package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ModuleExports
import com.seanshubin.jvmspec.domain.classfile.structure.ModuleOpens
import com.seanshubin.jvmspec.domain.classfile.structure.ModuleProvides
import com.seanshubin.jvmspec.domain.classfile.structure.ModuleRequires

interface JvmModuleAttribute : JvmAttribute {
    val moduleNameIndex: UShort
    val moduleFlags: Int
    val moduleVersionIndex: UShort
    val requiresCount: UShort
    val requires: List<ModuleRequires>
    val exportsCount: UShort
    val exports: List<ModuleExports>
    val opensCount: UShort
    val opens: List<ModuleOpens>
    val usesCount: UShort
    val usesIndex: List<UShort>
    val providesCount: UShort
    val provides: List<ModuleProvides>

    fun moduleName(): String
    fun moduleVersion(): String
}
