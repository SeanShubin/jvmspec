package com.seanshubin.jvmspec.domain.data

interface ConstantPoolLookup {
    fun getUtf8(constantIndex: UShort): String
}
