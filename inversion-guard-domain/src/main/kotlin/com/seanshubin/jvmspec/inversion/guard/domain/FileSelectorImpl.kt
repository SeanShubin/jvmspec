package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.filter.Filter
import com.seanshubin.jvmspec.domain.filter.FilterResult
import java.nio.file.Path
import java.util.stream.Stream

class FileSelectorImpl(
    private val baseDir: Path,
    private val files: FilesContract,
    private val classFileNameFilter: Filter
) : FileSelector {
    override fun <T> map(f: (Path) -> T): List<T> {
        return flatMap { path -> listOf(f(path)) }
    }

    override fun <T> flatMap(f: (Path) -> List<T>): List<T> {
        return files.walk(baseDir).filter { path ->
            files.isRegularFile(path)
        }.flatMap { path ->
            val filterResult = classFileNameFilter.match(path.toString())
            if (filterResult == FilterResult.INCLUDE_ONLY) {
                f(path).stream()
            } else {
                Stream.empty<T>()
            }
        }.toList()
    }
}
