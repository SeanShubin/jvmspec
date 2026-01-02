package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.jvm.JvmClass

class ClassProcessorImpl : ClassProcessor {
    override fun processClass(jvmClass: JvmClass): List<Command> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}