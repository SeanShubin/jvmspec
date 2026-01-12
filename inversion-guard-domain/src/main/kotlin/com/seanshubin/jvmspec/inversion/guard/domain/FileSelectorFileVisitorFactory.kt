package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

interface FileSelectorFileVisitorFactory {
    fun <T> create(
        process: (Path) -> List<T>,
        results: MutableList<T>
    ): FileSelectorFileVisitor<T>
}
