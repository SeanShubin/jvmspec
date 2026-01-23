package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ModuleHash

interface JvmModuleHashesAttribute : JvmAttribute {
    val algorithmIndex: UShort
    val modulesCount: UShort
    val modules: List<ModuleHash>
    fun algorithm(): String
}
