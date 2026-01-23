package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.BootstrapMethod

interface JvmBootstrapMethodsAttribute : JvmAttribute {
    val numBootstrapMethods: UShort
    val bootstrapMethods: List<BootstrapMethod>
}
