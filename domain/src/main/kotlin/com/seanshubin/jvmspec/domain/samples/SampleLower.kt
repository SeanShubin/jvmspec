package com.seanshubin.jvmspec.domain.samples

import java.nio.file.Files
import java.nio.file.Paths

class SampleLower {
    fun foo() {
        Files.list(Paths.get("."))
    }
}
