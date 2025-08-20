package com.seanshubin.jvmspec.domain.util

import java.io.DataInput

object DataInputExtensions {
    fun DataInput.readByteList(size: Int): List<Byte> {
        val byteArray = ByteArray(size)
        this.readFully(byteArray)
        return byteArray.toList()
    }
}
