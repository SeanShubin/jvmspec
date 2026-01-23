package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeSourceFileInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmSourceFileAttribute

class JvmSourceFileAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeSourceFileInfo: AttributeSourceFileInfo
) : JvmSourceFileAttribute {
    override val sourceFileIndex: UShort = attributeSourceFileInfo.sourceFileIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeSourceFileInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeSourceFileInfo.info
    }

    override fun sourceFileName(): String {
        return jvmClass.lookupUtf8(sourceFileIndex)
    }
}
