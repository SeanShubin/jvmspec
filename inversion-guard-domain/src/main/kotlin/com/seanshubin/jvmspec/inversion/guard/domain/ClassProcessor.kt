package com.seanshubin.jvmspec.inversion.guard.domain

interface ClassProcessor {
    fun processClass(classAnalysis: ClassAnalysis): List<Command>
}