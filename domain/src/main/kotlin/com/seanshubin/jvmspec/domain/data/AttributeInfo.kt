package com.seanshubin.jvmspec.domain.data

interface AttributeInfo {
    val attributeName: IndexName
    val attributeLength: Int
    val info: List<Byte>
    fun lines(index: Int): List<String>
}
