package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.asHex
import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.DataInput

data class ConstantUtf8Info(
    override val tag: ConstantPoolTag,
    val length: Short,
    val bytes: List<Byte>
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $length ${bytes.asHex()}"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.UTF8

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantUtf8Info {
            val length = input.readShort()
            val bytes = input.readByteList(length.toUShort().toInt())
            return ConstantUtf8Info(tag, length, bytes)
        }
    }
}
