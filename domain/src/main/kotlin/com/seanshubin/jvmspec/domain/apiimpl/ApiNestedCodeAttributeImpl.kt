package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiNestedCodeAttributeImpl(
    private val classFile: ClassFile,
    private val methodIndex: Int,
    private val outerAttributeIndex: Int,
    private val innerAttributeIndex: Int
) : ApiAttribute {
    private val methodInfo = classFile.methods[methodIndex]
    private val outerAttributeInfo = methodInfo.attributes[outerAttributeIndex] as AttributeCodeInfo
    private val innerAttributeInfo = outerAttributeInfo.attributes[innerAttributeIndex]
    override fun name(): String {
        val nameIndex = innerAttributeInfo.attributeIndex
        val name = classFile.constantPoolLookup.lookupUtf8Value(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return innerAttributeInfo.info
    }
}
