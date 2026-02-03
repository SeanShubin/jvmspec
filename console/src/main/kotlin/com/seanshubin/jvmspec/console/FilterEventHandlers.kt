package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.analysis.filtering.MatchedFilterEvent
import com.seanshubin.jvmspec.analysis.filtering.UnmatchedFilterEvent

object FilterEventHandlers {
    val nopMatchedHandler: (MatchedFilterEvent) -> Unit = { }
    val nopUnmatchedHandler: (UnmatchedFilterEvent) -> Unit = { }
}
