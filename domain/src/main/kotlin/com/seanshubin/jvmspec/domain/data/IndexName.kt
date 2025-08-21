package com.seanshubin.jvmspec.domain.data

data class IndexName(val index: UShort, val name: String) {
    fun line(): String = "[$index]$name"

    companion object {
        fun fromIndex(index: UShort, constantPoolLookup: ConstantPoolLookup): IndexName {
            val name = constantPoolLookup.getUtf8(index)
            return IndexName(index, name)
        }
        fun fromClassIndex(classIndex: UShort, constantPoolLookup: ConstantPoolLookup): IndexName {
            val name = constantPoolLookup.getClassName(classIndex)
            return IndexName(classIndex, name)
        }
    }
}