package com.seanshubin.jvmspec.domain.data

data class CodeCursor(val index:Int, val code: List<Byte>) {
    fun opCodeByte():UByte = code[index].toUByte()
    fun advance(amount: Int): CodeCursor = copy(index = index + amount)
    fun hasMore():Boolean = index < code.size
}