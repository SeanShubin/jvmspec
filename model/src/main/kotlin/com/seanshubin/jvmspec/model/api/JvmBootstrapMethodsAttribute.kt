package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.BootstrapMethod

interface JvmBootstrapMethodsAttribute : JvmAttribute {
    val numBootstrapMethods: UShort
    val bootstrapMethods: List<BootstrapMethod>
}
