package com.seanshubin.jvmspec.samples

import com.seanshubin.jvmspec.contract.FilesDelegate
import java.time.Clock

class Dependencies {
    val clock = Clock.systemUTC()
    val files = FilesDelegate.defaultInstance()
    val emit: (String) -> Unit = ::println
    val sample = Sample2(clock, files, emit)
}
