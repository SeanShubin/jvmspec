package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiAttributeImpl(
    private val classFile: ClassFile,
    private val attributeIndex: Int
) : ApiAttribute {
    private val attributeInfo = classFile.attributes[attributeIndex]
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        val name = classFile.constantPoolLookup.lookupUtf8Value(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return attributeInfo.info
    }
}
