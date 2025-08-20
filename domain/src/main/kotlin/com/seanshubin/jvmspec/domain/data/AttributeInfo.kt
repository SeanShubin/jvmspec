package com.seanshubin.jvmspec.domain.data

interface AttributeInfo {
    val attributeNameIndex: UShort
    val attributeLength: Int
    val info: List<Byte>
}
