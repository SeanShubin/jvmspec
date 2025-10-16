package com.seanshubin.jvmspec.domain.tree

data class Tree(
    val node: String,
    val children: List<Tree> = emptyList()
) {
    fun toLines(indent: (String) -> String): List<String> =
        listOf(node) + children.flatMap { it.toLines(indent).map(indent) }
}