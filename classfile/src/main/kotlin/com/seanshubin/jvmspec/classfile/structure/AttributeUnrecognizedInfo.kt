package com.seanshubin.jvmspec.classfile.structure

data class AttributeUnrecognizedInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo
