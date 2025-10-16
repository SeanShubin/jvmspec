package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiCodeAttribute
import com.seanshubin.jvmspec.domain.api.ApiField
import com.seanshubin.jvmspec.domain.api.Signature
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

class ApiFieldImpl(private val classFile: ClassFile, private val fieldIndex: Int) : ApiField {
    private val fieldInfo = classFile.fields[fieldIndex]
    private val constantPoolLookup = classFile.constantPoolLookup

    override fun accessFlags(): Set<AccessFlag> {
        return fieldInfo.accessFlags
    }

    override fun className(): String {
        return classFile.thisClassName()
    }

    override fun name(): String {
        val fieldNameIndex = fieldInfo.nameIndex
        val fieldName = constantPoolLookup.lookupUtf8Value(fieldNameIndex)
        return fieldName
    }

    override fun signature(): Signature {
        val fieldDescriptorIndex = fieldInfo.descriptorIndex
        val fieldDescriptor = constantPoolLookup.lookupUtf8Value(fieldDescriptorIndex)
        val signature = DescriptorParser.build(fieldDescriptor)
        return signature
    }

    override fun attributes(): List<ApiAttribute> {
        return fieldInfo.attributes.indices.map { attributeIndex ->
            ApiAttributeImpl(classFile, fieldIndex, attributeIndex)
        }
    }

    override fun code(): ApiCodeAttribute? {
        val codeAttributes = attributes().filter { it.name() == "Code" }
        return if (codeAttributes.isEmpty()) {
            null
        } else if (codeAttributes.size == 1) {
            codeAttributes[0].asCodeAttribute()
        } else {
            throw RuntimeException("Zero or one Code attributes expected, got ${codeAttributes.size}")
        }
    }
}
