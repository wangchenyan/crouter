package me.wcy.router.plugin

import me.wcy.router.annotation.CRouterConsts
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Created by wangchenyan.top on 2024/6/5.
 */
abstract class GenRouteRegistererTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputFolder: DirectoryProperty

    @TaskAction
    fun taskAction() {
        val filePath = CRouterConsts.FINAL_REGISTERER_CLASS_NAME
            .replace(".", "/")
            .plus(".kt")
        val outputFile = File(outputFolder.asFile.get(), filePath)
        outputFile.parentFile.mkdirs()
        outputFile.writeText(CRouterConsts.FINAL_REGISTERER_CLASS_CONTENT)
    }
}