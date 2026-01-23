package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class Annotation(
    val typeIndex: UShort,
    val numElementValuePairs: UShort,
    val elementValuePairs: List<ElementValuePair>
) {
    companion object {
        fun fromDataInput(
            input: DataInput,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): Annotation {
            val typeIndex = input.readUnsignedShort().toUShort()
            val numElementValuePairs = input.readUnsignedShort().toUShort()
            val elementValuePairs = (0 until numElementValuePairs.toInt()).map {
                ElementValuePair.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return Annotation(typeIndex, numElementValuePairs, elementValuePairs)
        }
    }
}
