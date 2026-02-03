package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.descriptor.Signature
import com.seanshubin.jvmspec.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.classfile.structure.FieldInfo
import com.seanshubin.jvmspec.model.api.JvmAttribute
import com.seanshubin.jvmspec.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmField

class JvmFieldImpl(
    private val jvmClass: JvmClass,
    private val fieldInfo: FieldInfo,
    private val attributeFactory: JvmAttributeFactory
) : JvmField {
    override fun accessFlags(): Set<AccessFlag> {
        return fieldInfo.accessFlags
    }

    override fun className(): String {
        return jvmClass.thisClassName
    }

    override fun name(): String {
        val fieldNameIndex = fieldInfo.nameIndex
        val fieldName = jvmClass.lookupUtf8(fieldNameIndex)
        return fieldName
    }

    override fun signature(): Signature {
        val fieldDescriptorIndex = fieldInfo.descriptorIndex
        val descriptor = jvmClass.lookupDescriptor(fieldDescriptorIndex)
        return Signature(className(), name(), descriptor)
    }

    override fun attributes(): List<JvmAttribute> {
        val toJvmAttribute = { attributeInfo: com.seanshubin.jvmspec.classfile.structure.AttributeInfo ->
            attributeFactory.createAttribute(jvmClass, attributeInfo)
        }
        return fieldInfo.attributes.map(toJvmAttribute)
    }
}
