package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.util.FilterResult
import java.nio.file.Path

class LineEmittingNotifications(
    private val emit: (Any?) -> Unit
) : Notifications {
    override fun filterEvent(file: Path, filterResult: FilterResult) {
        emit("$filterResult $file")
    }
}