package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.structure.AnnotationStructure.Annotation
import java.io.DataInput

data class ParameterAnnotation(
    val numAnnotations: UShort,
    val annotations: List<Annotation>
) {
    companion object {
        fun fromDataInput(
            input: DataInput,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): ParameterAnnotation {
            val numAnnotations = input.readUnsignedShort().toUShort()
            val annotations = (0 until numAnnotations.toInt()).map {
                Annotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return ParameterAnnotation(numAnnotations, annotations)
        }
    }
}
