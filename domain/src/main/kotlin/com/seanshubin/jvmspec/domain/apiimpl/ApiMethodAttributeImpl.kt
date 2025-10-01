package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiCodeAttribute
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiMethodAttributeImpl(
    private val classFile: ClassFile,
    private val methodIndex: Int,
    private val attributeIndex: Int
) : ApiAttribute {
    private val methodInfo = classFile.methods[methodIndex]
    private val attributeInfo = methodInfo.attributes[attributeIndex]
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        val name = classFile.constantPoolLookup.lookupUtf8Value(nameIndex)
        return name
    }

    override fun asCodeAttribute(): ApiCodeAttribute {
        return ApiCodeAttributeImpl(classFile, methodIndex, attributeIndex)
    }
}