package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toSanitizedString
import java.io.DataInput

data class ConstantUtf8Info(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val length: UShort,
    val utf8Value: String
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $length ${utf8Value.toSanitizedString()}"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.UTF8

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantUtf8Info {
            val length = input.readUnsignedShort().toUShort()
            val bytes: ByteArray = ByteArray(length.toInt())
            input.readFully(bytes)
            val utf8Value = String(bytes)
            return ConstantUtf8Info(tag, index, length, utf8Value)
        }
    }
}
