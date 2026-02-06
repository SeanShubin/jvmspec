package com.seanshubin.jvmspec.runtime.application

import com.seanshubin.jvmspec.classfile.structure.ClassFile
import com.seanshubin.jvmspec.di.contract.FilesContract
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmClassFactory
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormat
import java.io.DataInputStream
import java.nio.file.Path

class Runner(
    private val files: FilesContract,
    private val emit: (Any?) -> Unit,
    private val classFilePath: Path,
    private val classFactory: JvmClassFactory,
    private val format: JvmSpecFormat
) : Runnable {
    override fun run() {
        val bytes = files.readAllBytes(classFilePath)
        val input = DataInputStream(bytes.inputStream())
        val classFile = ClassFile.fromDataInput(classFilePath, input)
        val jvmClass = classFactory.createClass(classFile)
        val trees = format.classTreeList(jvmClass)
        val indent = { line: String -> "  $line" }
        trees.forEach { tree ->
            val lines = tree.toLines(indent)
            lines.forEach(emit)
        }
    }
}
