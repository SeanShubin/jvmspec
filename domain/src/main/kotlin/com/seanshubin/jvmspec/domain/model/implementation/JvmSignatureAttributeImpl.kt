package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeSignatureInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmSignatureAttribute

class JvmSignatureAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeSignatureInfo: AttributeSignatureInfo
) : JvmSignatureAttribute {
    override val signatureIndex: UShort = attributeSignatureInfo.signatureIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeSignatureInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeSignatureInfo.info
    }

    override fun signature(): String {
        return jvmClass.lookupUtf8(signatureIndex)
    }
}
