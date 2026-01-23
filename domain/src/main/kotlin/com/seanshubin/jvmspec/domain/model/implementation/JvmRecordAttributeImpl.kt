package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeRecordInfo
import com.seanshubin.jvmspec.domain.classfile.structure.RecordComponentInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmRecordAttribute

class JvmRecordAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRecordInfo: AttributeRecordInfo
) : JvmRecordAttribute {
    override val componentsCount: UShort = attributeRecordInfo.componentsCount
    override val components: List<RecordComponentInfo> = attributeRecordInfo.components

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRecordInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRecordInfo.info
    }
}
