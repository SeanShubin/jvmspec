package com.seanshubin.jvmspec.domain.data

data class ConstantValueAttribute(
    val attributeNameIndex: Short,
    val attributeLength: Int,
    val constantValueIndex: Short
) {
}