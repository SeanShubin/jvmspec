package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeSyntheticInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmSyntheticAttribute

class JvmSyntheticAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeSyntheticInfo: AttributeSyntheticInfo
) : JvmSyntheticAttribute {
    override fun name(): String {
        return jvmClass.lookupUtf8(attributeSyntheticInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeSyntheticInfo.info
    }
}
