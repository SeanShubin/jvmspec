package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.util.FilterResult
import java.nio.file.Path

class FileSelectorImpl(
    private val baseDir: Path,
    private val files: FilesContract,
    private val filter: (Path) -> FilterResult
) : FileSelector {
    override fun <T> flatMap(f: (Path) -> List<T>): List<T> {
        return files.walk(baseDir).filter { path ->
            files.isRegularFile(path)
        }.flatMap { path ->
            val filterResult = filter(path)
            if (filterResult == FilterResult.WHITELIST_ONLY) {
                f(path).stream()
            } else {
                emptyList<T>().stream()
            }
        }.toList()
    }
}
