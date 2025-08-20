package com.seanshubin.jvmspec.domain.data

data class CodeAttribute(
    val attributeNameIndex: Short,
    val attributeLength: Int,
    val maxStack: Short,
    val maxLocals: Short,
    val codeLength: Int,
    val code: List<Byte>,
    val exceptionTableLength: Short,
    val exceptionTables: List<ExceptionTable>,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
}