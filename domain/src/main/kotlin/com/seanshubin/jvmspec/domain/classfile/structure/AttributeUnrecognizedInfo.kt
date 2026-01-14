package com.seanshubin.jvmspec.domain.classfile.structure

data class AttributeUnrecognizedInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo
