package com.seanshubin.jvmspec.domain.util

import java.nio.file.Path

object PathUtil {
    fun Path.replaceExtension(oldExtension: String, newExtension: String): Path {
        val asString = this.toString()
        if (!asString.endsWith(".$oldExtension")) {
            throw IllegalArgumentException("Path does not end with extension '$oldExtension'")
        }
        val withoutExtension = asString.dropLast(oldExtension.length + 1)
        val withNewExtension = "$withoutExtension.$newExtension"
        return Path.of(withNewExtension)
    }
}
