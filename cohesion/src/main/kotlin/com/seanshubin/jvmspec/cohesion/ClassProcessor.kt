package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface ClassProcessor {
    fun processClass(jvmClass: JvmClass): List<Command>
}