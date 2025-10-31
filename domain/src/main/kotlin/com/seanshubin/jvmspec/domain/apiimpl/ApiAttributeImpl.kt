package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiAttribute
import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.data.AttributeInfo

class ApiAttributeImpl(
    private val apiClass: ApiClass,
    private val attributeInfo: AttributeInfo
) : ApiAttribute {
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        return apiClass.lookupUtf8(nameIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeInfo.info
    }
}
