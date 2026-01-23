package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeMethodParametersInfo
import com.seanshubin.jvmspec.domain.classfile.structure.MethodParameter
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmMethodParametersAttribute

class JvmMethodParametersAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeMethodParametersInfo: AttributeMethodParametersInfo
) : JvmMethodParametersAttribute {
    override val parametersCount: UByte = attributeMethodParametersInfo.parametersCount
    override val parameters: List<MethodParameter> = attributeMethodParametersInfo.parameters

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeMethodParametersInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeMethodParametersInfo.info
    }
}
