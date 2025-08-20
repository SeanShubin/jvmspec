package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantPackageInfo(
    override val tag: Byte,
    val nameIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 20

        fun fromDataInput(tag: Byte, input: DataInput): ConstantPackageInfo {
            val nameIndex = input.readShort()
            return ConstantPackageInfo(tag, nameIndex)
        }
    }
}