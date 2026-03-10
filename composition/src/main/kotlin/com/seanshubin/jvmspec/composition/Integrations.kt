package com.seanshubin.jvmspec.composition

import com.seanshubin.jvmspec.di.contract.FilesContract
import java.io.PrintStream
import java.time.Clock

interface Integrations {
    val commandLineArgs: Array<String>
    val files: FilesContract
    val clock: Clock
    val emit: (Any?) -> Unit
    val systemOut: PrintStream
    val systemErr: PrintStream
}
