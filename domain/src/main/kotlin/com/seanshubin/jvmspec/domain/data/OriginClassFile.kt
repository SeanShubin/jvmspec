package com.seanshubin.jvmspec.domain.data

import java.nio.file.Path

data class OriginClassFile(val path: Path) : Origin {
    override val id: String get() = "class-file:$path"
}