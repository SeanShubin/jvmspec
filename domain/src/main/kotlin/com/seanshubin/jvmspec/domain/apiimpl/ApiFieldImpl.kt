package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiField
import com.seanshubin.jvmspec.domain.api.Signature
import com.seanshubin.jvmspec.domain.data.FieldInfo
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

class ApiFieldImpl(
    private val apiClass: ApiClass,
    private val fieldInfo: FieldInfo
) : ApiField {
    override fun accessFlags(): Set<AccessFlag> {
        return fieldInfo.accessFlags
    }

    override fun className(): String {
        return apiClass.thisClassName
    }

    override fun name(): String {
        val fieldNameIndex = fieldInfo.nameIndex
        val fieldName = apiClass.lookupUtf8(fieldNameIndex)
        return fieldName
    }

    override fun signature(): Signature {
        val fieldDescriptorIndex = fieldInfo.descriptorIndex
        return apiClass.lookupSignature(fieldDescriptorIndex)
    }

    override fun attributes(): List<ApiAttribute> {
        return fieldInfo.attributes.map {
            ApiAttributeImpl(apiClass, it)
        }
    }
}
