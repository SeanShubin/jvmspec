package com.seanshubin.jvmspec.inversion.guard

interface ClassProcessor {
    fun processClass(classAnalysis: ClassAnalysis): List<Command>
}