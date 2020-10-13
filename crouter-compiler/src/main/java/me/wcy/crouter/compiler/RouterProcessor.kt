package me.wcy.crouter.compiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.Router
import me.wcy.router.annotation.RouterBuilder
import me.wcy.router.annotation.RouterLoader
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class)
class RouterProcessor : AbstractProcessor() {
    private lateinit var filer: Filer
    private lateinit var elementUtil: Elements
    private lateinit var typeUtil: Types
    private lateinit var moduleName: String
    private lateinit var defaultScheme: String
    private lateinit var defaultHost: String

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        filer = processingEnv.filer
        elementUtil = processingEnv.elementUtils
        typeUtil = processingEnv.typeUtils
        Log.setLogger(processingEnv.messager)

        val moduleName = processingEnv.options["moduleName"]
        val defaultScheme = processingEnv.options["defaultScheme"]
        val defaultHost = processingEnv.options["defaultHost"]
        if (moduleName == null || moduleName.isEmpty()
            || defaultScheme == null || defaultScheme.isEmpty()
            || defaultHost == null || defaultHost.isEmpty()
        ) {
            throw IllegalArgumentException(
                "[CRouter] Can not find apt argument 'moduleName', 'defaultScheme' or 'defaultHost', check if has add the code like this in module's build.gradle:\n" +
                        "    In Kotlin:\n" +
                        "    \n" +
                        "    kapt {\n" +
                        "        arguments {\n" +
                        "          arg(\"moduleName\", project.name)\n" +
                        "          arg(\"defaultScheme\", 'scheme')\n" +
                        "          arg(\"defaultHost\", 'host')\n" +
                        "        }\n" +
                        "    }\n"
            )
        }

        this.moduleName = moduleName
        this.defaultScheme = defaultScheme
        this.defaultHost = defaultHost

        Log.w("[CRouter] Start to deal module ${this.moduleName}, defaultScheme=$defaultScheme, defaultHost=$defaultHost")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val supportAnnotationTypes = mutableSetOf<String>()
        supportAnnotationTypes.add(Router::class.java.canonicalName)
        return supportAnnotationTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val routerElements = roundEnv.getElementsAnnotatedWith(Router::class.java)
        return parseRouter(routerElements)
    }

    private fun parseRouter(routerElements: MutableSet<out Element>?): Boolean {
        if (routerElements == null || routerElements.size == 0) {
            return false
        }

        Log.w("[CRouter] Found routers, size is ${routerElements.size}")

        val activityType = elementUtil.getTypeElement("android.app.Activity")
        val routerBuilderCn = ClassName.get(RouterBuilder::class.java)

        /**
         * Param type: Set<Router>
         */
        val inputMapTypeName = ParameterizedTypeName.get(
            ClassName.get(Set::class.java),
            ClassName.get(Route::class.java)
        )

        /**
         * Param name: routerSet
         */
        val groupParamSpec =
            ParameterSpec.builder(inputMapTypeName, ProcessorUtils.PARAM_NAME).build()

        /**
         * Method: @Override public void loadRouter(Set<Route> routerSet)
         */
        val loadRouterMethodBuilder = MethodSpec.methodBuilder(ProcessorUtils.METHOD_NAME)
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(groupParamSpec)

        for (element in routerElements) {
            val typeMirror = element.asType()
            val router = element.getAnnotation(Router::class.java)

            if (typeUtil.isSubtype(typeMirror, activityType.asType())) {
                Log.w("[CRouter] Found activity router: $typeMirror")

                val activityCn = ClassName.get(element as TypeElement)
                var routerUrl = ProcessorUtils.assembleRouterUrl(router, defaultScheme, defaultHost)
                routerUrl = ProcessorUtils.escapeUrl(routerUrl)

                /**
                 * Statement: routerSet.add(RouterBuilder.buildRouter(url, needLogin, target));
                 */
                loadRouterMethodBuilder.addStatement(
                    "\$N.add(\$T.buildRouter(\$N, \$N, \$T.class))", ProcessorUtils.PARAM_NAME,
                    routerBuilderCn, routerUrl, router.needLogin.toString(), activityCn
                )
            }
        }

        /**
         * Write to file
         */
        JavaFile.builder(
            ProcessorUtils.PACKAGE_NAME,
            TypeSpec.classBuilder("RouterLoader\$$moduleName")
                .addJavadoc(ProcessorUtils.JAVADOC)
                .addSuperinterface(ClassName.get(RouterLoader::class.java))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(loadRouterMethodBuilder.build())
                .build()
        )
            .build()
            .writeTo(filer)

        return true
    }
}
