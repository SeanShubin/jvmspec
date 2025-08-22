package com.seanshubin.jvmspec.domain.data

interface ConstantInfo {
    val tag: ConstantPoolTag
    val index: Int
    val entriesTaken: Int
    fun line(): String
    fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String
}
