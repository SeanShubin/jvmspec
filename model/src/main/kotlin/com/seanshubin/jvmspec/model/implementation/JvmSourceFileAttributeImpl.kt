package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeSourceFileInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmSourceFileAttribute

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
