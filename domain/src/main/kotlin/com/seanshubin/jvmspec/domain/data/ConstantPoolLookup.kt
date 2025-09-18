package com.seanshubin.jvmspec.domain.data

interface ConstantPoolLookup {
    fun lookupUtf8Value(index: UShort): String
    fun className(index: UShort): String
    fun ref(index: UShort): Triple<String, String, String>
    fun nameAndType(index: UShort): Pair<String, String>
    fun utf8Line(index: UShort): String
    fun classLine(index: UShort): String
    fun nameAndTypeLine(index: UShort): String
    fun referenceIndexLine(index: UShort): String
    fun lines(): List<String>
    fun line(index: UShort): String
}
