package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

interface ConstantInfo {
    val tag: ConstantPoolTag
    val index: Int
    val entriesTaken: Int
    fun line(): String
    fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String
}
