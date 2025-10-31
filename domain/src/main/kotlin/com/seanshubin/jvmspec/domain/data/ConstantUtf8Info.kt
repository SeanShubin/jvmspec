package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantUtf8Info(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val length: UShort,
    val utf8Value: String
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.UTF8

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantUtf8Info {
            val length = input.readUnsignedShort().toUShort()
            val bytes: ByteArray = ByteArray(length.toInt())
            input.readFully(bytes)
            val utf8Value = String(bytes)
            return ConstantUtf8Info(tag, index, length, utf8Value)
        }
    }
}
