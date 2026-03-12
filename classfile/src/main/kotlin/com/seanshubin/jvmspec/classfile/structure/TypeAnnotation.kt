package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.ElementValuePair
import java.io.DataInput

sealed class TargetInfo {
    data class TypeParameterTarget(val typeParameterIndex: UByte) : TargetInfo()
    data class SupertypeTarget(val supertypeIndex: UShort) : TargetInfo()
    data class TypeParameterBoundTarget(
        val typeParameterIndex: UByte,
        val boundIndex: UByte
    ) : TargetInfo()

    object EmptyTarget : TargetInfo()
    data class FormalParameterTarget(val formalParameterIndex: UByte) : TargetInfo()
    data class ThrowsTarget(val throwsTypeIndex: UShort) : TargetInfo()
    data class LocalvarTarget(
        val table: List<LocalvarTargetEntry>
    ) : TargetInfo() {
        data class LocalvarTargetEntry(
            val startPc: UShort,
            val length: UShort,
            val index: UShort
        )
    }

    data class CatchTarget(val exceptionTableIndex: UShort) : TargetInfo()
    data class OffsetTarget(val offset: UShort) : TargetInfo()
    data class TypeArgumentTarget(
        val offset: UShort,
        val typeArgumentIndex: UByte
    ) : TargetInfo()
}

data class TypePath(val path: List<TypePathEntry>) {
    data class TypePathEntry(
        val typePathKind: UByte,
        val typeArgumentIndex: UByte
    )
}

data class TypeAnnotation(
    val targetType: UByte,
    val targetInfo: TargetInfo,
    val typePath: TypePath,
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

            // Read target_info based on target_type
            val targetInfo = when (targetType.toInt()) {
                0x00, 0x01 -> {
                    val typeParameterIndex = input.readUnsignedByte().toUByte()
                    TargetInfo.TypeParameterTarget(typeParameterIndex)
                }

                0x10 -> {
                    val supertypeIndex = input.readUnsignedShort().toUShort()
                    TargetInfo.SupertypeTarget(supertypeIndex)
                }

                0x11, 0x12 -> {
                    val typeParameterIndex = input.readUnsignedByte().toUByte()
                    val boundIndex = input.readUnsignedByte().toUByte()
                    TargetInfo.TypeParameterBoundTarget(typeParameterIndex, boundIndex)
                }

                0x13, 0x14, 0x15 -> {
                    TargetInfo.EmptyTarget
                }

                0x16 -> {
                    val formalParameterIndex = input.readUnsignedByte().toUByte()
                    TargetInfo.FormalParameterTarget(formalParameterIndex)
                }

                0x17 -> {
                    val throwsTypeIndex = input.readUnsignedShort().toUShort()
                    TargetInfo.ThrowsTarget(throwsTypeIndex)
                }

                0x40, 0x41 -> {
                    val tableLength = input.readUnsignedShort().toUShort()
                    val table = (0 until tableLength.toInt()).map {
                        val startPc = input.readUnsignedShort().toUShort()
                        val length = input.readUnsignedShort().toUShort()
                        val index = input.readUnsignedShort().toUShort()
                        TargetInfo.LocalvarTarget.LocalvarTargetEntry(startPc, length, index)
                    }
                    TargetInfo.LocalvarTarget(table)
                }

                0x42 -> {
                    val exceptionTableIndex = input.readUnsignedShort().toUShort()
                    TargetInfo.CatchTarget(exceptionTableIndex)
                }

                0x43, 0x44, 0x45, 0x46 -> {
                    val offset = input.readUnsignedShort().toUShort()
                    TargetInfo.OffsetTarget(offset)
                }

                0x47, 0x48, 0x49, 0x4A, 0x4B -> {
                    val offset = input.readUnsignedShort().toUShort()
                    val typeArgumentIndex = input.readUnsignedByte().toUByte()
                    TargetInfo.TypeArgumentTarget(offset, typeArgumentIndex)
                }

                else -> throw IllegalArgumentException("Unknown target_type: 0x${targetType.toString(16)}")
            }

            val targetPathLength = input.readUnsignedByte().toUByte()
            val typePathEntries = (0 until targetPathLength.toInt()).map {
                val typePathKind = input.readUnsignedByte().toUByte()
                val typeArgumentIndex = input.readUnsignedByte().toUByte()
                TypePath.TypePathEntry(typePathKind, typeArgumentIndex)
            }
            val typePath = TypePath(typePathEntries)

            val typeIndex = input.readUnsignedShort().toUShort()
            val numElementValuePairs = input.readUnsignedShort().toUShort()
            val elementValuePairs = (0 until numElementValuePairs.toInt()).map {
                ElementValuePair.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }

            return TypeAnnotation(
                targetType,
                targetInfo,
                typePath,
                typeIndex,
                numElementValuePairs,
                elementValuePairs
            )
        }
    }
}
