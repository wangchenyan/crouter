package me.wcy.crouter.processor

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
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.jvm.jvmName
import me.wcy.router.annotation.CRouterConsts
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.RouteMeta

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

        val moduleName = environment.options["moduleName"].orEmpty()
        val defaultScheme = environment.options["defaultScheme"].orEmpty()
        val defaultHost = environment.options["defaultHost"].orEmpty()

        require(
            moduleName.isNotEmpty()
                    && defaultScheme.isNotEmpty()
                    && defaultHost.isNotEmpty()
        ) {
            "CRouter Processor Can not find ksp argument 'moduleName', 'defaultScheme' or 'defaultHost', check if has add the code like this in module's build.gradle.kts:\n" +
                    "\n" +
                    "    ksp {\n" +
                    "       arg(\"moduleName\", project.name)\n" +
                    "       arg(\"defaultScheme\", 'scheme')\n" +
                    "       arg(\"defaultHost\", 'host')\n" +
                    "    }" +
                    "\n"
        }

        this.moduleName = ProcessorUtils.formatModuleName(moduleName)
        this.defaultScheme = defaultScheme
        this.defaultHost = defaultHost

        logger.warn("-------- CRouter Processor --------")
        logger.warn("Start to deal module ${this.moduleName}, defaultScheme=$defaultScheme, defaultHost=$defaultHost")
        return this
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val routeElements = resolver.getSymbolsWithAnnotation(Route::class.java.name).toList()
        if (routeElements.isEmpty()) {
            return emptyList()
        }

        logger.warn("Found routes, size is ${routeElements.size}")

        /**
         * Method: fun register()
         */
        val loadRouteMethodBuilder =
            FunSpec.builder(CRouterConsts.MODULE_REGISTERER_CLASS_METHOD_NAME)
                .addKdoc(CRouterConsts.JAVADOC + "\n")

        val routeMetaCn = RouteMeta::class.asClassName()

        routeElements.forEach {
            checkDeclaration(it)
            val declaration = it as KSDeclaration
            val className = declaration.toClassName()
            logger.warn("Found route: ${className.canonicalName}")

            val route = declaration.getAnnotationsByType(Route::class).first()
            var routeUrl = ProcessorUtils.assembleRouteUrl(route, defaultScheme, defaultHost)
            routeUrl = ProcessorUtils.escapeUrl(routeUrl)

            /**
             * Statement: CRouter.register(RouteMeta(url, target, needLogin))
             */
            loadRouteMethodBuilder.addStatement(
                "CRouter.register(%T(%L, %T::class, %L))",
                routeMetaCn,
                routeUrl,
                className,
                route.needLogin.toString()
            )
        }

        val genClassName = CRouterConsts.MODULE_REGISTERER_CLASS_NAME_PREFIX + moduleName

        /**
         * Write to file
         */
        val fileSpec = FileSpec.builder(CRouterConsts.MODULE_REGISTERER_PACKAGE_NAME, genClassName)
            .jvmName(genClassName)
            .addImport("me.wcy.router", "CRouter")
            .addFunction(loadRouteMethodBuilder.build())
            .build()

        codeGenerator
            .createNewFile(Dependencies.ALL_FILES, fileSpec.packageName, fileSpec.name)
            .write(fileSpec.toString().toByteArray())

        logger.warn("Generate code into ${fileSpec.packageName}.${fileSpec.name}")

        return emptyList()
    }

    private fun checkDeclaration(annotated: KSAnnotated) {
        check(annotated is KSClassDeclaration) {
            "Type [${annotated}] with annotation [${Route::class.java.name}] should be a class"
        }
        checkNotNull(
            annotated
                .getAllSuperTypes()
                .find {
                    it.declaration.toClassName().canonicalName in supportTypes
                }
        ) {
            "Un support route type [${annotated.toClassName().canonicalName}], only support activity or fragment"
        }
    }
}
