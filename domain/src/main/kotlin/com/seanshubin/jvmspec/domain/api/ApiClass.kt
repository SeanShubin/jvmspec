package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.apiimpl.DescriptorParser
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

interface ApiClass {
    val origin: String
    val magic: Int
    val minorVersion: Int
    val majorVersion: Int
    val thisClassName: String
    val superClassName: String
    fun methods(): List<ApiMethod>
    val constants: SortedMap<UShort, ApiConstant.Constant>
    val accessFlags: Set<AccessFlag>
    fun interfaces(): List<ApiConstant.Constant>
    fun fields(): List<ApiField>
    fun attributes(): List<ApiAttribute>

    fun lookupClassName(classIndex: UShort): String {
        val classConstant = constants.getValue(classIndex) as ApiConstant.Constant
        val utf8Constant = classConstant.parts[0] as ApiConstant.Constant
        val utf8Value = utf8Constant.parts[0] as ApiConstant.StringValue
        return utf8Value.s
    }

    fun lookupUtf8(utf8Index: UShort): String {
        val utf8Constant = constants.getValue(utf8Index) as ApiConstant.Constant
        val utf8Value = utf8Constant.parts[0] as ApiConstant.StringValue
        return utf8Value.s
    }

    fun lookupSignature(descriptorIndex: UShort): Signature {
        val descriptorUtf8 = lookupUtf8(descriptorIndex)
        val descriptor = DescriptorParser.build(descriptorUtf8)
        return descriptor
    }
}
