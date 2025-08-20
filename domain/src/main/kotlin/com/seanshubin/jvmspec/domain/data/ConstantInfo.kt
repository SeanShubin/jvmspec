package com.seanshubin.jvmspec.domain.data

interface ConstantInfo {
    val tag: ConstantPoolTag
    val entriesTaken: Int
    fun line(): String
}
