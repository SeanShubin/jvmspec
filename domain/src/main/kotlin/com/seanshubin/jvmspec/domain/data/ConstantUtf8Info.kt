package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.DataInput

data class ConstantUtf8Info(
    override val tag: Byte,
    val length: Short,
    val bytes: List<Byte>
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 1

        fun fromDataInput(tag: Byte, input: DataInput): ConstantUtf8Info {
            val length = input.readShort()
            val bytes = input.readByteList(length.toUShort().toInt())
            return ConstantUtf8Info(tag, length, bytes)
        }
    }
}
