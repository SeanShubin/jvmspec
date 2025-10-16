package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiCodeAttribute
import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.api.Signature
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

class ApiMethodImpl(private val classFile: ClassFile, private val methodIndex: Int) : ApiMethod {
    private val methodInfo = classFile.methods[methodIndex]
    private val constantPoolLookup = classFile.constantPoolLookup
    override fun accessFlags(): Set<AccessFlag> {
        return methodInfo.accessFlags
    }

    override fun className(): String {
        return classFile.thisClassName()
    }

    override fun name(): String {
        val methodNameIndex = methodInfo.nameIndex
        val methodName = constantPoolLookup.lookupUtf8Value(methodNameIndex)
        return methodName
    }

    override fun signature(): Signature {
        val methodDescriptorIndex = methodInfo.descriptorIndex
        val methodDescriptor = constantPoolLookup.lookupUtf8Value(methodDescriptorIndex)
        val signature = DescriptorParser.build(methodDescriptor)
        return signature
    }

    override fun attributes(): List<ApiAttribute> {
        return methodInfo.attributes.indices.map { attributeIndex ->
            ApiMethodAttributeImpl(classFile, methodIndex, attributeIndex)
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
