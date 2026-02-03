package com.seanshubin.jvmspec.classfile.structure

interface AttributeInfo {
    val attributeIndex: UShort
    val attributeLength: Int
    val info: List<Byte>
}
