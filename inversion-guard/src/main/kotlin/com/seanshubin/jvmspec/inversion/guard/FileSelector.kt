package com.seanshubin.jvmspec.inversion.guard

import java.nio.file.Path

interface FileSelector {
    fun <T> flatMap(f: (Path) -> List<T>): List<T>
}
