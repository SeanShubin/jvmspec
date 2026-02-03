package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.RecordComponentInfo

interface JvmRecordAttribute : JvmAttribute {
    val componentsCount: UShort
    val components: List<RecordComponentInfo>
}
