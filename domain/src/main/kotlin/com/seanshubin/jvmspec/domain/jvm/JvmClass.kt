package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

interface JvmClass {
    val origin: JvmOrigin
    val magic: Int
    val minorVersion: Int
    val majorVersion: Int
    val thisClassName: String
    val superClassName: String
    fun methods(): List<JvmMethod>
    val constants: SortedMap<UShort, JvmConstant.Constant>
    val accessFlags: Set<AccessFlag>
    fun interfaces(): List<JvmConstant.Constant>
    fun fields(): List<JvmField>
    fun attributes(): List<JvmAttribute>

    fun lookupClassName(classIndex: UShort): String {
        val classConstant = constants.getValue(classIndex) as JvmConstant.Constant
        val utf8Constant = classConstant.parts[0] as JvmConstant.Constant
        val utf8Value = utf8Constant.parts[0] as JvmConstant.StringValue
        return utf8Value.s
    }

    fun lookupUtf8(utf8Index: UShort): String {
        val utf8Constant = constants.getValue(utf8Index) as JvmConstant.Constant
        val utf8Value = utf8Constant.parts[0] as JvmConstant.StringValue
        return utf8Value.s
    }

    fun lookupSignature(descriptorIndex: UShort): Signature {
        val descriptorUtf8 = lookupUtf8(descriptorIndex)
        val descriptor = DescriptorParser.build(descriptorUtf8)
        return descriptor
    }
}
