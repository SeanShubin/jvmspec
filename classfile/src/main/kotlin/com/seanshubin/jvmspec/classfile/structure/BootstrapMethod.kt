package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

data class BootstrapMethod(
    val bootstrapMethodRef: UShort,
    val numBootstrapArguments: UShort,
    val bootstrapArguments: List<UShort>
) {
    companion object {
        fun fromDataInput(input: DataInput): BootstrapMethod {
            val bootstrapMethodRef = input.readUnsignedShort().toUShort()
            val numBootstrapArguments = input.readUnsignedShort().toUShort()
            val bootstrapArguments = (0 until numBootstrapArguments.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return BootstrapMethod(bootstrapMethodRef, numBootstrapArguments, bootstrapArguments)
        }
    }
}
