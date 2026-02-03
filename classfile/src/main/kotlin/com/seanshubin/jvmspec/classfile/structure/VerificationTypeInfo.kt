package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

sealed class VerificationTypeInfo {
    abstract val tag: Int

    data class TopVariable(override val tag: Int = 0) : VerificationTypeInfo()
    data class IntegerVariable(override val tag: Int = 1) : VerificationTypeInfo()
    data class FloatVariable(override val tag: Int = 2) : VerificationTypeInfo()
    data class DoubleVariable(override val tag: Int = 3) : VerificationTypeInfo()
    data class LongVariable(override val tag: Int = 4) : VerificationTypeInfo()
    data class NullVariable(override val tag: Int = 5) : VerificationTypeInfo()
    data class UninitializedThisVariable(override val tag: Int = 6) : VerificationTypeInfo()
    data class ObjectVariable(override val tag: Int = 7, val cpoolIndex: UShort) : VerificationTypeInfo()
    data class UninitializedVariable(override val tag: Int = 8, val offset: UShort) : VerificationTypeInfo()

    companion object {
        fun fromDataInput(input: DataInput): VerificationTypeInfo {
            val tag = input.readUnsignedByte()
            return when (tag) {
                0 -> TopVariable()
                1 -> IntegerVariable()
                2 -> FloatVariable()
                3 -> DoubleVariable()
                4 -> LongVariable()
                5 -> NullVariable()
                6 -> UninitializedThisVariable()
                7 -> {
                    val cpoolIndex = input.readUnsignedShort().toUShort()
                    ObjectVariable(cpoolIndex = cpoolIndex)
                }

                8 -> {
                    val offset = input.readUnsignedShort().toUShort()
                    UninitializedVariable(offset = offset)
                }

                else -> throw IllegalArgumentException("Unknown verification_type_info tag: $tag")
            }
        }
    }
}
