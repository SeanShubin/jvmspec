package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiCodeAttribute
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiFieldAttributeImpl(
    private val classFile: ClassFile,
    private val fieldIndex: Int,
    private val attributeIndex: Int
) : ApiAttribute {
    private val fieldInfo = classFile.fields[fieldIndex]
    private val attributeInfo = fieldInfo.attributes[attributeIndex]
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        val name = classFile.constantPoolLookup.lookupUtf8Value(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return attributeInfo.info
    }

    override fun asCodeAttribute(): ApiCodeAttribute {
        return ApiCodeAttributeImpl(classFile, fieldIndex, attributeIndex)
    }
}