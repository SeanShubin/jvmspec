package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeMethodParametersInfo
import com.seanshubin.jvmspec.classfile.structure.MethodParameter
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmMethodParametersAttribute

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
