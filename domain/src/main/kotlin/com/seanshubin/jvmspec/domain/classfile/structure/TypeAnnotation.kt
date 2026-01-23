package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.structure.AnnotationStructure.ElementValuePair
import java.io.DataInput

data class TypeAnnotation(
    val targetType: UByte,
    val targetInfo: List<Byte>,  // Simplified - actual structure varies by targetType
    val targetPathLength: UByte,
    val targetPath: List<Byte>,  // Simplified - actual structure is path array
    val typeIndex: UShort,
    val numElementValuePairs: UShort,
    val elementValuePairs: List<ElementValuePair>
) {
    companion object {
        fun fromDataInput(
            input: DataInput,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): TypeAnnotation {
            val targetType = input.readUnsignedByte().toUByte()

            // Read target_info (size varies by target_type, simplified here)
            val targetInfoSize = when (targetType.toInt()) {
                0x00, 0x01 -> 1  // type_parameter_target
                0x10 -> 2         // supertype_target
                0x11, 0x12 -> 2   // type_parameter_bound_target
                0x13, 0x14, 0x15 -> 0  // empty_target
                0x16 -> 1         // formal_parameter_target
                0x17 -> 2         // throws_target
                0x40, 0x41 -> 4   // localvar_target (simplified)
                0x42 -> 2         // catch_target
                0x43, 0x44, 0x45, 0x46 -> 2  // offset_target
                0x47, 0x48, 0x49, 0x4A, 0x4B -> 4  // type_argument_target
                else -> 0
            }
            val targetInfo = (0 until targetInfoSize).map { input.readByte() }

            val targetPathLength = input.readUnsignedByte().toUByte()
            val targetPath = (0 until targetPathLength.toInt() * 2).map { input.readByte() }

            val typeIndex = input.readUnsignedShort().toUShort()
            val numElementValuePairs = input.readUnsignedShort().toUShort()
            val elementValuePairs = (0 until numElementValuePairs.toInt()).map {
                ElementValuePair.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }

            return TypeAnnotation(
                targetType,
                targetInfo,
                targetPathLength,
                targetPath,
                typeIndex,
                numElementValuePairs,
                elementValuePairs
            )
        }
    }
}
