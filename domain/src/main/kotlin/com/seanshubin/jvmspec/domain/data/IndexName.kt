package com.seanshubin.jvmspec.domain.data

data class IndexName(val index: UShort, val name: String) {
    fun line(): String = "[$index]$name"

    companion object {
        fun fromIndex(index: UShort, constantPoolLookup: ConstantPoolLookup): IndexName {
            val name = constantPoolLookup.getUtf8(index)
            return IndexName(index, name)
        }
    }
}