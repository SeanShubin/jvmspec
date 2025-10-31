package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.MethodInfo
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

class ApiMethodImpl(
    private val apiClass: ApiClass,
    private val methodInfo: MethodInfo
) : ApiMethod {
    override fun accessFlags(): Set<AccessFlag> {
        return methodInfo.accessFlags
    }

    override fun className(): String {
        return apiClass.thisClassName
    }

    override fun name(): String {
        val methodNameIndex = methodInfo.nameIndex
        return apiClass.lookupUtf8(methodNameIndex)
    }

    override fun signature(): Signature {
        val methodDescriptorIndex = methodInfo.descriptorIndex
        return apiClass.lookupSignature(methodDescriptorIndex)
    }

    override fun attributes(): List<ApiAttribute> {
        return methodInfo.attributes.map { attribute ->
            when (attribute) {
                is AttributeCodeInfo -> ApiCodeAttributeImpl(apiClass, attribute)
                else -> ApiAttributeImpl(apiClass, attribute)
            }
        }
    }

    override fun code(): ApiCodeAttribute? {
        val codeAttributes = attributes().filterIsInstance<ApiCodeAttribute>()
        return if (codeAttributes.isEmpty()) {
            null
        } else if (codeAttributes.size == 1) {
            codeAttributes[0]
        } else {
            throw RuntimeException("Zero or one Code attributes expected, got ${codeAttributes.size}")
        }
    }
}
