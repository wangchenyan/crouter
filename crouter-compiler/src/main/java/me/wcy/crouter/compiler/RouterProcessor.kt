package me.wcy.crouter.compiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.RouteInfo
import me.wcy.router.annotation.RouteInfoBuilder
import me.wcy.router.annotation.RouteLoader
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
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
        if (moduleName.isNullOrEmpty()
            || defaultScheme.isNullOrEmpty()
            || defaultHost.isNullOrEmpty()
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

        this.moduleName = ProcessorUtils.formatModuleName(moduleName)
        this.defaultScheme = defaultScheme
        this.defaultHost = defaultHost

        Log.i("[CRouter] Start to deal module ${this.moduleName}, defaultScheme=$defaultScheme, defaultHost=$defaultHost")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val supportAnnotationTypes = mutableSetOf<String>()
        supportAnnotationTypes.add(Route::class.java.canonicalName)
        return supportAnnotationTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val routeElements = roundEnv.getElementsAnnotatedWith(Route::class.java)
        return parseRoutes(routeElements)
    }

    private fun parseRoutes(routeElements: MutableSet<out Element>?): Boolean {
        if (routeElements == null || routeElements.size == 0) {
            return false
        }

        Log.i("[CRouter] Found routes, size is ${routeElements.size}")

        val activityType = elementUtil.getTypeElement("android.app.Activity")
        val fragmentType = elementUtil.getTypeElement("android.app.Fragment")
        val fragmentXType = elementUtil.getTypeElement("androidx.fragment.app.Fragment")
        val routeInfoBuilderCn = ClassName.get(RouteInfoBuilder::class.java)

        /**
         * Param type: Set<RouteInfo>
         */
        val inputMapTypeName = ParameterizedTypeName.get(
            ClassName.get(Set::class.java),
            ClassName.get(RouteInfo::class.java)
        )

        /**
         * Param name: routeSet
         */
        val groupParamSpec =
            ParameterSpec.builder(inputMapTypeName, ProcessorUtils.PARAM_NAME).build()

        /**
         * Method: @Override public void loadRoute(Set<RouteInfo> routeSet)
         */
        val loadRouteMethodBuilder = MethodSpec.methodBuilder(ProcessorUtils.METHOD_NAME)
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(groupParamSpec)

        for (element in routeElements) {
            val typeMirror = element.asType()
            val route = element.getAnnotation(Route::class.java)

            if (typeUtil.isSubtype(typeMirror, activityType.asType())
                || typeUtil.isSubtype(typeMirror, fragmentType.asType())
                || typeUtil.isSubtype(typeMirror, fragmentXType.asType())
            ) {
                Log.i("[CRouter] Found route: $typeMirror")

                val className = ClassName.get(element as TypeElement)
                var routeUrl = ProcessorUtils.assembleRouteUrl(route, defaultScheme, defaultHost)
                routeUrl = ProcessorUtils.escapeUrl(routeUrl)

                /**
                 * Statement: routeSet.add(RouteInfoBuilder.buildRouteInfo(url, needLogin, target));
                 */
                loadRouteMethodBuilder.addStatement(
                    "\$N.add(\$T.buildRouteInfo(\$N, \$T.class, \$N))", ProcessorUtils.PARAM_NAME,
                    routeInfoBuilderCn, routeUrl, className, route.needLogin.toString()
                )
            }
        }

        /**
         * Write to file
         */
        JavaFile.builder(
            ProcessorUtils.PACKAGE_NAME,
            TypeSpec.classBuilder("RouteLoader\$$moduleName")
                .addJavadoc(ProcessorUtils.JAVADOC)
                .addSuperinterface(ClassName.get(RouteLoader::class.java))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(loadRouteMethodBuilder.build())
                .build()
        )
            .build()
            .writeTo(filer)

        return true
    }
}
