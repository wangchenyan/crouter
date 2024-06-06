package me.wcy.router.plugin

import me.wcy.router.annotation.CRouterConsts
import org.gradle.api.file.Directory
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.InstructionAdapter

/**
 * Created by wangchenyan.top on 2024/6/5.
 */
class InsertCodeVisitor(
    nextVisitor: ClassVisitor,
    private val inputDirs: List<Directory>,
    private val genDirName: String
) : ClassVisitor(Opcodes.ASM9, nextVisitor) {

    init {
        println("-------- CRouter Plugin Input Dirs --------")
        inputDirs.forEach {
            println(it.asFile.path)
        }
    }

    private val classNamePrefix by lazy {
        CRouterConsts.MODULE_REGISTERER_PACKAGE_NAME
            .replace(".", "/")
            .plus("/")
            .plus(CRouterConsts.MODULE_REGISTERER_CLASS_NAME_PREFIX)
    }

    private val validClasses by lazy {
        inputDirs.asSequence()
            .flatMap { dir -> dir.asFileTree.matching { it.include("**/**.kt") } }
            .mapNotNull { file ->
                return@mapNotNull file.absolutePath
                    .replace("\\", "/")
                    .substringAfter(genDirName)
                    .substringAfter("kotlin/")
                    .removeSuffix(".kt")
                    .takeIf { it.startsWith(classNamePrefix) }
            }
            .toList()
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == CRouterConsts.FINAL_REGISTERER_CLASS_METHOD_NAME) {
            return object : InstructionAdapter(Opcodes.ASM9, mv) {
                override fun visitCode() {
                    println("-------- CRouter Plugin Insert Code --------")
                    validClasses.forEach { clazz ->
                        println("Insert $clazz")
                        invokestatic(
                            clazz,
                            CRouterConsts.MODULE_REGISTERER_CLASS_METHOD_NAME,
                            "()V",
                            false
                        )
                    }
                    super.visitCode()
                }
            }
        }
        return mv
    }
}