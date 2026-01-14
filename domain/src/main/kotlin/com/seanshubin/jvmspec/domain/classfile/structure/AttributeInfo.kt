package com.seanshubin.jvmspec.domain.classfile.structure

interface AttributeInfo {
    val attributeIndex: UShort
    val attributeLength: Int
    val info: List<Byte>
}
