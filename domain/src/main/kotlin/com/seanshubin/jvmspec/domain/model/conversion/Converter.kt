package com.seanshubin.jvmspec.domain.model.conversion

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.implementation.JvmClassImpl
import java.io.DataInput
import java.io.DataInputStream
import java.nio.file.Path

object Converter {
    fun Path.toDataInput(files: FilesContract): DataInput {
        val inputStream = files.newInputStream(this)
        return DataInputStream(inputStream)
    }

    fun DataInput.toClassFile(origin: Path): ClassFile = ClassFile.fromDataInput(origin, this)

    fun ClassFile.toJvmClass(): JvmClass = JvmClassImpl(this)

    fun Path.toJvmClass(files: FilesContract, origin: Path): JvmClass =
        toDataInput(files).toClassFile(origin).toJvmClass()
}
