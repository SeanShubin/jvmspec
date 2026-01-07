package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.nio.file.Path
import java.util.*

interface JvmClass {
    val origin: Path
    val magic: Int
    val minorVersion: Int
    val majorVersion: Int
    val thisClassName: String
    val superClassName: String
    fun methods(): List<JvmMethod>
    val constants: SortedMap<UShort, JvmConstant>
    val accessFlags: Set<AccessFlag>
    fun interfaces(): List<JvmConstant>
    fun fields(): List<JvmField>
    fun attributes(): List<JvmAttribute>

    fun complexity(): Int {
        return methods().sumOf { method -> method.complexity() }
    }
    fun lookupClassName(classIndex: UShort): String {
        val classConstant = constants.getValue(classIndex) as JvmConstant.JvmConstantClass
        return classConstant.nameUtf8.value
    }

    fun lookupUtf8(utf8Index: UShort): String {
        val utf8Constant = constants.getValue(utf8Index) as JvmConstant.JvmConstantUtf8
        return utf8Constant.value
    }

    fun lookupSignature(descriptorIndex: UShort): Signature {
        val descriptorUtf8 = lookupUtf8(descriptorIndex)
        val descriptor = DescriptorParser.build(descriptorUtf8)
        return descriptor
    }
}
