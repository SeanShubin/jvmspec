package com.seanshubin.jvmspec.domain.samples

import com.seanshubin.jvmspec.domain.files.FilesDelegate
import java.time.Clock

class Dependencies {
    val clock = Clock.systemUTC()
    val files = FilesDelegate
    val emit: (String) -> Unit = ::println
    val sample = Sample2(clock, files, emit)
}
