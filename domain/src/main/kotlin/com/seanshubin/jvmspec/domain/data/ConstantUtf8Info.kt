package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.asSanitizedString
import java.io.DataInput

data class ConstantUtf8Info(
    override val tag: ConstantPoolTag,
    val length: UShort,
    val utf8Value: String
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $length ${utf8Value.asSanitizedString()}"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.UTF8

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantUtf8Info {
            val length = input.readUnsignedShort().toUShort()
            val bytes: ByteArray = ByteArray(length.toInt())
            input.readFully(bytes)
            val utf8Value = String(bytes)
            return ConstantUtf8Info(tag, length, utf8Value)
        }
    }
}
