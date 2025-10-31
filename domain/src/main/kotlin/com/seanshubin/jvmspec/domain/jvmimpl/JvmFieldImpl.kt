package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.FieldInfo
import com.seanshubin.jvmspec.domain.jvm.JvmAttribute
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmField
import com.seanshubin.jvmspec.domain.jvm.Signature
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

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
        return jvmClass.lookupSignature(fieldDescriptorIndex)
    }

    override fun attributes(): List<JvmAttribute> {
        return fieldInfo.attributes.map {
            JvmAttributeImpl(jvmClass, it)
        }
    }
}
