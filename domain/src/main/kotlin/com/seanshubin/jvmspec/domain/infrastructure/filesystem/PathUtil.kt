package com.seanshubin.jvmspec.domain.infrastructure.filesystem

import java.nio.file.Path

object PathUtil {
    fun Path.removeExtension(expectedExtension: String): String {
        val asString = this.toString()
        if (!asString.endsWith(".$expectedExtension")) {
            throw IllegalArgumentException("Path does not end with extension '$expectedExtension'")
        }
        val withoutExtension = asString.dropLast(expectedExtension.length + 1)
        return withoutExtension
    }
}
