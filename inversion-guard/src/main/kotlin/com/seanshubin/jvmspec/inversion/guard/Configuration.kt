package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.rules.CategoryRule
import java.nio.file.Path

data class Configuration(
    val baseDir: Path,
    val outputDir: Path,
    val include: List<String>,
    val exclude: List<String>,
    val core: List<String>,
    val boundary: List<String>,
    val categoryRuleSet: Map<String, CategoryRule>
)
