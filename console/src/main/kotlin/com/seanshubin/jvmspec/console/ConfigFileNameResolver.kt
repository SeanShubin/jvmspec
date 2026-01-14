package com.seanshubin.jvmspec.console

object ConfigFileNameResolver {
    fun resolveConfigFileName(args: Array<String>): String =
        args.getOrNull(0) ?: "config.json"
}
