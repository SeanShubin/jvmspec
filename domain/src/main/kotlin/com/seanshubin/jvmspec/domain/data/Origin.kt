package com.seanshubin.jvmspec.domain.data

import java.nio.file.Path

sealed interface Origin {
    data class OriginClassFile(val path: Path) : Origin
}
