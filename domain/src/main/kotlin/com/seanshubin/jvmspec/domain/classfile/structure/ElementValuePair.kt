package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ElementValuePair(
    val elementNameIndex: UShort,
    val value: ElementValue
) {
    companion object {
        fun fromDataInput(
            input: DataInput,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): ElementValuePair {
            val elementNameIndex = input.readUnsignedShort().toUShort()
            val value = ElementValue.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            return ElementValuePair(elementNameIndex, value)
        }
    }
}
