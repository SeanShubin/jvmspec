package com.seanshubin.jvmspec.classfile.io

import java.io.DataInput

object DataInputExtensions {
    fun DataInput.readByteList(size: Int): List<Byte> {
        val byteArray = ByteArray(size)
        this.readFully(byteArray)
        return byteArray.toList()
    }
}
