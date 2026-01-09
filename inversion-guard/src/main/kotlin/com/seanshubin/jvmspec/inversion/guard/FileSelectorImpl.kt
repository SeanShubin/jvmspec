package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.filter.Filter
import com.seanshubin.jvmspec.domain.filter.FilterResult
import java.nio.file.Path

class FileSelectorImpl(
    private val baseDir: Path,
    private val files: FilesContract,
    private val classFileNameFilter: Filter
) : FileSelector {
    override fun <T> map(f: (Path) -> T): List<T> {
        return files.walk(baseDir).filter { path ->
            files.isRegularFile(path)
        }.flatMap { path ->
            val filterResult = classFileNameFilter.match(path.toString())
            if (filterResult == FilterResult.INCLUDE_ONLY) {
                listOf(f(path)).stream()
            } else {
                emptyList<T>().stream()
            }
        }.toList()
    }

    override fun <T> flatMap(f: (Path) -> List<T>): List<T> {
        return files.walk(baseDir).filter { path ->
            files.isRegularFile(path)
        }.flatMap { path ->
            val filterResult = classFileNameFilter.match(path.toString())
            if (filterResult == FilterResult.INCLUDE_ONLY) {
                f(path).stream()
            } else {
                emptyList<T>().stream()
            }
        }.toList()
    }
}
