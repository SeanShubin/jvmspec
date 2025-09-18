package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

data class ClassFile(
    val magic: UInt,
    val minorVersion: UShort,
    val majorVersion: UShort,
    val constantPoolCount: UShort,
    val constantPool: List<ConstantInfo>,
    val constantPoolLookup: ConstantPoolLookup,
    val accessFlags: Set<AccessFlag>,
    val thisClass: UShort,
    val superClass: UShort,
    val interfacesCount: UShort,
    val interfaces: List<UShort>,
    val fieldsCount: UShort,
    val fields: List<FieldInfo>,
    val methodsCount: UShort,
    val methods: List<MethodInfo>,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(): List<String> {
        val interfaceLines = interfaces.mapIndexed { index, interfaceValue ->
            "[$index] ${constantPoolLookup.line(interfaceValue)}"
        }
        return listOf(
            "magic: ${magic.toDecHex()}",
            "minorVersion: ${minorVersion.toDecHex()}",
            "majorVersion: ${majorVersion.toDecHex()}",
            "constantPoolCount: ${constantPoolCount.toDecHex()}",
            "constantPool:",
            *constantPoolLookup.lines().map(indent).toTypedArray(),
            "accessFlags: $accessFlags",
            "thisClass: ${constantPoolLookup.line(thisClass)}",
            "superClass: ${constantPoolLookup.line(superClass)}",
            "interfacesCount: ${interfacesCount.toDecHex()}",
            "interfaces:",
            *interfaceLines.map(indent).toTypedArray(),
            "fieldsCount: $fieldsCount",
            *fields.flatMapIndexed { index, field ->
                field.lines(index, constantPoolLookup)
            }.map(indent).toTypedArray(),
            "methodsCount: $methodsCount",
            *methods.flatMapIndexed { index, method ->
                method.lines(index, constantPoolLookup)
            }.map(indent).toTypedArray(),
            "attributesCount: $attributesCount",
            *attributes.flatMapIndexed { index, attribute ->
                attribute.lines(index, constantPoolLookup)
            }.map(indent).toTypedArray()
        )
    }

    fun thisClassName(): String {
        return constantPoolLookup.className(thisClass)
    }

    companion object {
        fun fromDataInput(input: DataInput): ClassFile {
            val magic = input.readInt().toUInt()
            val minorVersion = input.readUnsignedShort().toUShort()
            val majorVersion = input.readUnsignedShort().toUShort()
            val constantPoolCount = input.readUnsignedShort().toUShort()
            val constantPool = readConstantPool(input, constantPoolCount)
            val constantPoolLookup = ConstantPoolLookupImpl(constantPool)
            val accessFlagsMask = input.readUnsignedShort().toUShort()
            val accessFlags = AccessFlag.fromMask(accessFlagsMask)
            val thisClass = input.readUnsignedShort().toUShort()
            val superClass = input.readUnsignedShort().toUShort()
            val interfacesCount = input.readUnsignedShort().toUShort()
            val interfaces = List(interfacesCount.toInt()) {
                input.readUnsignedShort().toUShort()
            }
            val fieldsCount = input.readUnsignedShort().toUShort()
            val fields = List(fieldsCount.toInt()) { FieldInfo.fromDataInput(input, constantPoolLookup) }
            val methodsCount = input.readUnsignedShort().toUShort()
            val methods = List(methodsCount.toInt()) { MethodInfo.fromDataInput(input, constantPoolLookup) }
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return ClassFile(
                magic,
                minorVersion,
                majorVersion,
                constantPoolCount,
                constantPool,
                constantPoolLookup,
                accessFlags,
                thisClass,
                superClass,
                interfacesCount,
                interfaces,
                fieldsCount,
                fields,
                methodsCount,
                methods,
                attributesCount,
                attributes
            )
        }

        fun readConstantPool(input: DataInput, count: UShort): List<ConstantInfo> {
            var index = 1
            val intCount = count.toInt()
            val constantPool = mutableListOf<ConstantInfo>()
            while (index < intCount) {
                val constantInfo = ConstantInfoFactory.fromDataInput(index, input)
                constantPool.add(constantInfo)
                index += constantInfo.entriesTaken
            }
            return constantPool
        }
    }
}
