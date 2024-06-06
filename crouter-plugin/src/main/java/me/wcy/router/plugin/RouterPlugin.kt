package me.wcy.router.plugin

import com.android.build.api.AndroidPluginVersion
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by wangchenyan.top on 2024/6/5.
 */
class RouterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)

        require(isApp) { "CRouter Plugin only support application module" }

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        val gradleVersion = project.gradle.gradleVersion
        println("-------- CRouter Plugin Env --------")
        println("Gradle version $gradleVersion")
        println("${androidComponents.pluginVersion}")
        println("JDK version ${System.getProperty("java.version")}")

        require(androidComponents.pluginVersion >= AndroidPluginVersion(7, 4, 0)) {
            "AGP version must be at least 7.4 or higher. current version ${androidComponents.pluginVersion}"
        }

        androidComponents.onVariants { variant ->
            val addSourceTaskProvider = project.tasks.register(
                "${variant.name}GenRouterRegisterer",
                GenRouteRegistererTask::class.java
            )
            variant.sources.java?.addGeneratedSourceDirectory(
                addSourceTaskProvider,
                GenRouteRegistererTask::outputFolder
            )

            val generatedDir = "generated/ksp/"
            variant.instrumentation.transformClassesWith(
                RouterAsmClassVisitor::class.java,
                InstrumentationScope.PROJECT
            ) { param ->
                param.genDirName.set(generatedDir)
                val list = project.rootProject.subprojects.plus(project)
                    .map { it.layout.buildDirectory.dir(generatedDir).get() }
                param.inputFiles.set(list)
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
            variant.instrumentation.excludes.addAll(
                "androidx/**",
                "android/**",
                "com/google/**",
            )
        }
    }
}