package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.analysis.filtering.Filter
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class FileSelectorFileVisitor<T>(
    private val directoryFilter: Filter,
    private val classFileNameFilter: Filter,
    private val process: (Path) -> List<T>,
    private val results: MutableList<T>
) : SimpleFileVisitor<Path>() {
    override fun preVisitDirectory(
        dir: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        val dirFilterResult = directoryFilter.match(dir.toString())
        return if (dirFilterResult.contains("skipDir")) {
            FileVisitResult.SKIP_SUBTREE
        } else {
            FileVisitResult.CONTINUE
        }
    }

    override fun visitFile(
        file: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        val fileFilterResult = classFileNameFilter.match(file.toString())
        if (fileFilterResult == setOf("includeFile")) {
            results.addAll(process(file))
        }
        return FileVisitResult.CONTINUE
    }
}
