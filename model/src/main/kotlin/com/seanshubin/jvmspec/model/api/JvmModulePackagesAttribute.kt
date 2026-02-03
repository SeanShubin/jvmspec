package com.seanshubin.jvmspec.model.api

interface JvmModulePackagesAttribute : JvmAttribute {
    val packageCount: UShort
    val packageIndex: List<UShort>
}
