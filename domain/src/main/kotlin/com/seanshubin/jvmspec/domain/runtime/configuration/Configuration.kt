package com.seanshubin.jvmspec.domain.runtime.configuration

import java.nio.file.Path

data class Configuration(
    val inputDir: Path,
    val outputDir: Path,
    val include: List<String>,
    val exclude: List<String>
)
