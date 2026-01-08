package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface ClassAnalyzer {
    fun analyzeClass(jvmClass: JvmClass): ClassAnalysis
}
