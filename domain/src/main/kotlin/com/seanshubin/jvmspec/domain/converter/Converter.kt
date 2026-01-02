package com.seanshubin.jvmspec.domain.converter

import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.data.Origin
import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvmimpl.JvmClassImpl
import java.io.DataInput
import java.io.DataInputStream
import java.nio.file.Path

fun Path.toDataInput(files: FilesContract): DataInput {
    val inputStream = files.newInputStream(this)
    return DataInputStream(inputStream)
}

fun DataInput.toClassFile(origin: Origin): ClassFile = ClassFile.fromDataInput(origin, this)

fun ClassFile.toJvmClass(): JvmClass = JvmClassImpl(this)

fun Path.toJvmClass(files: FilesContract, origin: Origin): JvmClass =
    toDataInput(files).toClassFile(origin).toJvmClass()
