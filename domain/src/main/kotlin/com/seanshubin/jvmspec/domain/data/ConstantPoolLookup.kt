package com.seanshubin.jvmspec.domain.data

interface ConstantPoolLookup {
    fun lookupUtf8Value(index: UShort): String
    fun lookupClassNameValue(index: UShort): String
    fun lookupUtf8(index: UShort): ConstantUtf8Info
    fun lookupCLass(index: UShort): ConstantClassInfo
    fun lookupNameAndType(index: UShort): ConstantNameAndTypeInfo
    fun utf8Line(index: UShort): String
    fun classLine(index: UShort): String
    fun nameAndTypeLine(index: UShort): String
    fun referenceIndexLine(index: UShort): String
    fun lines(): List<String>
}
