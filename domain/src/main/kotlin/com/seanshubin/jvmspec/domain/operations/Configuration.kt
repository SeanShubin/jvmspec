package com.seanshubin.jvmspec.domain.operations

import java.nio.file.Path

data class Configuration(
    val inputDir: Path,
    val outputDir: Path,
    val includeMethod: List<String>,
    val excludeMethod: List<String>,
    val includeClass: List<String>,
    val excludeClass: List<String>
)
