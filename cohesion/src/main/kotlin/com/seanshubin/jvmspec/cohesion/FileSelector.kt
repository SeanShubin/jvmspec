package com.seanshubin.jvmspec.cohesion

import java.nio.file.Path

interface FileSelector {
    fun <T> flatMap(f: (Path) -> List<T>): List<T>
}
