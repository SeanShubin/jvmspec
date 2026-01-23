package com.seanshubin.jvmspec.domain.model.api

interface JvmModulePackagesAttribute : JvmAttribute {
    val packageCount: UShort
    val packageIndex: List<UShort>
}
