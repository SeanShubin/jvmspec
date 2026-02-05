package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.di.contract.FilesContract
import java.nio.file.Path

class FileSelectorImpl(
    private val baseDir: Path,
    private val files: FilesContract,
    private val visitorFactory: FileSelectorFileVisitorFactory
) : FileSelector {
    override fun <T> map(f: (Path) -> T): List<T> {
        return flatMap { path -> listOf(f(path)) }
    }

    override fun <T> flatMap(f: (Path) -> List<T>): List<T> {
        val results = mutableListOf<T>()
        val visitor = visitorFactory.create(f, results)
        files.walkFileTree(baseDir, visitor)
        return results
    }
}
