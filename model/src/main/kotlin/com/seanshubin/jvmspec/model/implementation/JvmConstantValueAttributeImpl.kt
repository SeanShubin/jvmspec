package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeConstantValueInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmConstant
import com.seanshubin.jvmspec.model.api.JvmConstantValueAttribute

class JvmConstantValueAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeConstantValueInfo: AttributeConstantValueInfo
) : JvmConstantValueAttribute {
    override val constantValueIndex: UShort = attributeConstantValueInfo.constantValueIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeConstantValueInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeConstantValueInfo.info
    }

    override fun constantValue(): String {
        val constant = jvmClass.constants.getValue(constantValueIndex)
        return when (constant) {
            is JvmConstant.JvmConstantInteger -> constant.value.toString()
            is JvmConstant.JvmConstantFloat -> constant.value.toString()
            is JvmConstant.JvmConstantLong -> constant.value.toString()
            is JvmConstant.JvmConstantDouble -> constant.value.toString()
            is JvmConstant.JvmConstantString -> constant.value
            else -> constant.toString()
        }
    }
}
