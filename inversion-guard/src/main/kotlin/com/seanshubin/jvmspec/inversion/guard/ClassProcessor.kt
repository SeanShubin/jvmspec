package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface ClassProcessor {
    fun processClass(jvmClass: JvmClass): List<Command>
}