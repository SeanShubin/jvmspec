package com.seanshubin.jvmspec.domain.data

interface AttributeInfo {
    val attributeIndex: UShort
    val attributeLength: Int
    val info: List<Byte>
    fun lines(index: Int, constantPoolLookup: ConstantPoolLookup): List<String>
}
