package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.util.FilterResult
import com.seanshubin.jvmspec.domain.util.RegexUtil
import java.nio.file.Path

class FilterImpl(
    private val whiteListPatterns: List<String>,
    private val blackListPatterns: List<String>,
    private val filterEvent: (Path, FilterResult) -> Unit
) : (Path) -> FilterResult {
    private val filterString = RegexUtil.createMatchFunctionFromList(whiteListPatterns, blackListPatterns)
    override fun invoke(file: Path): FilterResult {
        val filterResult = filterString(file.toString())
        filterEvent(file, filterResult)
        return filterResult
    }
}
