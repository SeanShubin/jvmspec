package com.seanshubin.jvmspec.domain.jvm

import java.nio.file.Path

sealed interface JvmOrigin:Comparable<JvmOrigin> {
    data class JvmOriginClass(val path: Path):JvmOrigin {
        override fun compareTo(other: JvmOrigin): Int =
            when (other) {
                is JvmOriginClass -> this.path.compareTo(other.path)
            }
    }
}
