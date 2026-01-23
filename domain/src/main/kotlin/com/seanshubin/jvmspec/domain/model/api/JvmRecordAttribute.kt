package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.RecordComponentInfo

interface JvmRecordAttribute : JvmAttribute {
    val componentsCount: UShort
    val components: List<RecordComponentInfo>
}
