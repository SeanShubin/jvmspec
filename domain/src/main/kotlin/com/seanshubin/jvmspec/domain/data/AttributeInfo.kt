package com.seanshubin.jvmspec.domain.data

interface AttributeInfo {
    val attributeNameIndex: Short
    val attributeLength: Int
    val info: List<Byte>
}
