package com.seanshubin.jvmspec.infrastructure.collections

data class Tree(
    val node: String,
    val children: List<Tree> = emptyList()
) {
    fun toLines(indent: (String) -> String): List<String> =
        listOf(node) + children.flatMap { it.toLines(indent).map(indent) }
}