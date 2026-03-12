package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.composition.Integrations
import com.seanshubin.jvmspec.di.contract.FilesContract
import com.seanshubin.jvmspec.di.delegate.FilesDelegate
import java.io.PrintStream
import java.nio.file.Path
import java.time.Clock

class StressTestIntegrations(
    override val commandLineArgs: Array<String>,
    outputPath: Path
) : Integrations {
    override val files: FilesContract = FilesDelegate.defaultInstance()
    override val clock: Clock = Clock.systemUTC()
    private val outputStream: PrintStream
    override val emit: (Any?) -> Unit
    override val systemOut: PrintStream
    override val systemErr: PrintStream = System.err

    init {
        files.createDirectories(outputPath.parent)
        outputStream = PrintStream(files.newOutputStream(outputPath))
        systemOut = outputStream
        emit = { value -> outputStream.println(value) }
    }

    fun close() {
        outputStream.close()
    }
}
