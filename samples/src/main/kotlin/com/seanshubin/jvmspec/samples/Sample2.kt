package com.seanshubin.jvmspec.samples

import com.seanshubin.jvmspec.contract.FilesContract
import java.nio.file.Paths
import java.time.Clock

class Sample2(
    private val clock: Clock,
    private val files: FilesContract,
    private val emit: (String) -> Unit
) {

    fun run(args: Array<String>) {
        val startTime = clock.instant()
        val fileName = args[0]
        val path = Paths.get(fileName)
        val charset = Charsets.UTF_8
        val target = files.readString(path, charset)
        val greeting = "Hello, $target!"
        emit(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        emit(timeTakenMessage)
    }
}
