package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.ModuleHash

interface JvmModuleHashesAttribute : JvmAttribute {
    val algorithmIndex: UShort
    val modulesCount: UShort
    val modules: List<ModuleHash>
    fun algorithm(): String
}
