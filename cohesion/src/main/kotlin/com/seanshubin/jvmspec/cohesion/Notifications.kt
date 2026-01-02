package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.util.FilterResult
import java.nio.file.Path

interface Notifications {
    fun filterEvent(file: Path, filterResult: FilterResult)
}
