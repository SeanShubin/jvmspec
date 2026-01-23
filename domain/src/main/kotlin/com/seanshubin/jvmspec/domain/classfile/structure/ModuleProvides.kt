package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ModuleProvides(
    val providesIndex: UShort,
    val providesWithCount: UShort,
    val providesWithIndex: List<UShort>
) {
    companion object {
        fun fromDataInput(input: DataInput): ModuleProvides {
            val providesIndex = input.readUnsignedShort().toUShort()
            val providesWithCount = input.readUnsignedShort().toUShort()
            val providesWithIndex = (0 until providesWithCount.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return ModuleProvides(providesIndex, providesWithCount, providesWithIndex)
        }
    }
}
