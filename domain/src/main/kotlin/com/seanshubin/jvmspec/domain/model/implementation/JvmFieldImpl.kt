package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.descriptor.Signature
import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.domain.classfile.structure.FieldInfo
import com.seanshubin.jvmspec.domain.model.api.JvmAttribute
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmField

class JvmFieldImpl(
    private val jvmClass: JvmClass,
    private val fieldInfo: FieldInfo
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
        return fieldInfo.attributes.map {
            JvmAttributeImpl(jvmClass, it)
        }
    }
}
