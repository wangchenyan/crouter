package me.wcy.crouter.compiler

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.RouteInfo
import me.wcy.router.annotation.RouteLoader

@DelicateKotlinPoetApi("")
@KspExperimental
class RouterProcessor : SymbolProcessor, SymbolProcessorProvider {
    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var moduleName: String
    private lateinit var defaultScheme: String
    private lateinit var defaultHost: String

    private val supportTypes = setOf(
        "android.app.Activity",
        "android.app.Fragment",
        "androidx.fragment.app.Fragment"
    )

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        logger = environment.logger
        codeGenerator = environment.codeGenerator

        val moduleName = environment.options["moduleName"]
        val defaultScheme = environment.options["defaultScheme"]
        val defaultHost = environment.options["defaultHost"]
        if (moduleName.isNullOrEmpty()
            || defaultScheme.isNullOrEmpty()
            || defaultHost.isNullOrEmpty()
        ) {
            throw IllegalArgumentException(
                "[CRouter] Can not find ksp argument 'moduleName', 'defaultScheme' or 'defaultHost', check if has add the code like this in module's build.gradle.kts:\n" +
                        "\n" +
                        "    ksp {\n" +
                        "       arg(\"moduleName\", project.name)\n" +
                        "       arg(\"defaultScheme\", 'scheme')\n" +
                        "       arg(\"defaultHost\", 'host')\n" +
                        "    }" +
                        "\n"
            )
        }

        this.moduleName = ProcessorUtils.formatModuleName(moduleName)
        this.defaultScheme = defaultScheme
        this.defaultHost = defaultHost

        logger.warn("[CRouter] Start to deal module ${this.moduleName}, defaultScheme=$defaultScheme, defaultHost=$defaultHost")
        return this
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val routeElements = resolver.getSymbolsWithAnnotation(Route::class.java.name).toList()
        if (routeElements.isEmpty()) {
            return emptyList()
        }

        logger.warn("[CRouter] Found routes, size is ${routeElements.size}")

        /**
         * Param type: MutableSet<RouteInfo>
         */
        val setTypeName = ClassName(
            "kotlin.collections",
            "MutableSet"
        ).parameterizedBy(RouteInfo::class.asTypeName())

        /**
         * Param name: routeSet: MutableSet<RouteInfo>
         */
        val groupParamSpec = ParameterSpec.builder(ProcessorUtils.PARAM_NAME, setTypeName)
            .build()

        /**
         * Method: override fun loadRoute(routeSet: MutableSet<RouteInfo>)
         */
        val loadRouteMethodBuilder = FunSpec.builder(ProcessorUtils.METHOD_NAME)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(groupParamSpec)

        val routeInfoCn = RouteInfo::class.asClassName()

        routeElements.forEach {
            checkDeclaration(it)
            val declaration = it as KSDeclaration
            val className = declaration.toClassName()
            logger.warn("[CRouter] Found route: ${className.canonicalName}")

            val route = declaration.getAnnotationsByType(Route::class).first()
            var routeUrl = ProcessorUtils.assembleRouteUrl(route, defaultScheme, defaultHost)
            routeUrl = ProcessorUtils.escapeUrl(routeUrl)

            /**
             * Statement: routeSet.add(RouteInfo(url, needLogin, target))
             */
            loadRouteMethodBuilder.addStatement(
                "%N.add(%T(%L, %T::class, %L))",
                ProcessorUtils.PARAM_NAME,
                routeInfoCn,
                routeUrl,
                className,
                route.needLogin.toString()
            )
        }

        /**
         * Write to file
         */
        val fileSpec = FileSpec.builder(ProcessorUtils.PACKAGE_NAME, "RouteLoader\$$moduleName")
            .addType(
                TypeSpec.classBuilder("RouteLoader\$$moduleName")
                    .addKdoc(ProcessorUtils.JAVADOC)
                    .addSuperinterface(RouteLoader::class.java)
                    .addFunction(loadRouteMethodBuilder.build())
                    .build()
            )
            .build()

        val file =
            codeGenerator.createNewFile(Dependencies.ALL_FILES, fileSpec.packageName, fileSpec.name)
        file.write(fileSpec.toString().toByteArray())

        return emptyList()
    }

    private fun checkDeclaration(annotated: KSAnnotated) {
        check(annotated is KSClassDeclaration) {
            "Type [${annotated}] with annotation [${Route::class.java.name}] should be a class"
        }
        checkNotNull(annotated.getAllSuperTypes().find {
            it.declaration.toClassName().canonicalName in supportTypes
        }) {
            "Un support route type [${annotated.toClassName().canonicalName}], only support activity or fragment"
        }
    }
}
