package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface ClassAnalyzer {
    fun analyzeClass(jvmClass: JvmClass): ClassAnalysis
}
