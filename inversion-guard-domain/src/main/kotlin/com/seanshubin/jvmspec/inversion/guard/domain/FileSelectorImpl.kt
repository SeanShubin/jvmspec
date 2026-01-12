package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.filter.Filter
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class FileSelectorImpl(
    private val baseDir: Path,
    private val files: FilesContract,
    private val classFileNameFilter: Filter,
    private val directoryFilter: Filter
) : FileSelector {
    override fun <T> map(f: (Path) -> T): List<T> {
        return flatMap { path -> listOf(f(path)) }
    }

    override fun <T> flatMap(f: (Path) -> List<T>): List<T> {
        val results = mutableListOf<T>()

        files.walkFileTree(baseDir, object : SimpleFileVisitor<Path>() {
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
                    results.addAll(f(file))
                }
                return FileVisitResult.CONTINUE
            }
        })

        return results
    }
}
