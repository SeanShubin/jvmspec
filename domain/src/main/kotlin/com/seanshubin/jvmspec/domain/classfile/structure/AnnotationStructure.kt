package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

sealed class AnnotationStructure {

    data class Annotation(
        val typeIndex: UShort,
        val numElementValuePairs: UShort,
        val elementValuePairs: List<ElementValuePair>
    ) : AnnotationStructure() {
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

    data class ElementValuePair(
        val elementNameIndex: UShort,
        val value: ElementValue
    ) : AnnotationStructure() {
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

    sealed class ElementValue : AnnotationStructure() {
        abstract val tag: Char

        data class ConstValueIndex(
            override val tag: Char,
            val constValueIndex: UShort
        ) : ElementValue()

        data class EnumConstValue(
            override val tag: Char,
            val typeNameIndex: UShort,
            val constNameIndex: UShort
        ) : ElementValue()

        data class ClassInfoIndex(
            override val tag: Char,
            val classInfoIndex: UShort
        ) : ElementValue()

        data class AnnotationValue(
            override val tag: Char,
            val annotationValue: Annotation
        ) : ElementValue()

        data class ArrayValue(
            override val tag: Char,
            val numValues: UShort,
            val values: List<ElementValue>
        ) : ElementValue()

        companion object {
            fun fromDataInput(
                input: DataInput,
                constantPoolMap: Map<UShort, ConstantInfo>,
                attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
            ): ElementValue {
                val tag = input.readByte().toInt().toChar()
                return when (tag) {
                    'B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 's' -> {
                        val constValueIndex = input.readUnsignedShort().toUShort()
                        ConstValueIndex(tag, constValueIndex)
                    }

                    'e' -> {
                        val typeNameIndex = input.readUnsignedShort().toUShort()
                        val constNameIndex = input.readUnsignedShort().toUShort()
                        EnumConstValue(tag, typeNameIndex, constNameIndex)
                    }

                    'c' -> {
                        val classInfoIndex = input.readUnsignedShort().toUShort()
                        ClassInfoIndex(tag, classInfoIndex)
                    }

                    '@' -> {
                        val annotationValue =
                            Annotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
                        AnnotationValue(tag, annotationValue)
                    }

                    '[' -> {
                        val numValues = input.readUnsignedShort().toUShort()
                        val values = (0 until numValues.toInt()).map {
                            fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
                        }
                        ArrayValue(tag, numValues, values)
                    }

                    else -> throw IllegalArgumentException("Unknown element_value tag: $tag")
                }
            }
        }
    }
}
