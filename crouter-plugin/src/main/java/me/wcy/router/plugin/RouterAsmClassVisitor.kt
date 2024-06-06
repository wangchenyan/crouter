package me.wcy.router.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import me.wcy.router.annotation.CRouterConsts
import org.gradle.api.file.Directory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.objectweb.asm.ClassVisitor

/**
 * Created by wangchenyan.top on 2024/6/5.
 */
abstract class RouterAsmClassVisitor :
    AsmClassVisitorFactory<RouterAsmClassVisitor.RouterParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        if (classContext.currentClassData.className == CRouterConsts.FINAL_REGISTERER_CLASS_NAME) {
            val inputFiles = parameters.get().inputFiles.get()
            val genDirName = parameters.get().genDirName.get()
            return InsertCodeVisitor(nextClassVisitor, inputFiles, genDirName)
        }
        return nextClassVisitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className == CRouterConsts.FINAL_REGISTERER_CLASS_NAME
    }

    interface RouterParameters : InstrumentationParameters {

        @get:Internal
        val genDirName: Property<String>

        @get:Internal
        val inputFiles: ListProperty<Directory>
    }
}
