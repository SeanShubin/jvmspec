package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.io.DataInput
import java.nio.file.Path

data class ClassFile(
    val origin: Path,
    val magic: UInt,
    val minorVersion: UShort,
    val majorVersion: UShort,
    val constantPoolCount: UShort,
    val constantPool: List<ConstantInfo>,
    val constantPoolMap: Map<UShort, ConstantInfo>,
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
    companion object {
        fun fromDataInput(origin: Path, input: DataInput): ClassFile {
            val magic = input.readInt().toUInt()
            val minorVersion = input.readUnsignedShort().toUShort()
            val majorVersion = input.readUnsignedShort().toUShort()
            val constantPoolCount = input.readUnsignedShort().toUShort()
            val constantPool = readConstantPool(input, constantPoolCount)
            val constantPoolMap = constantPool.associateBy { it.index }
            val accessFlagsMask = input.readUnsignedShort().toUShort()
            val accessFlags = AccessFlag.fromMask(accessFlagsMask)
            val thisClass = input.readUnsignedShort().toUShort()
            val superClass = input.readUnsignedShort().toUShort()
            val interfacesCount = input.readUnsignedShort().toUShort()
            val interfaces = List(interfacesCount.toInt()) {
                input.readUnsignedShort().toUShort()
            }
            val fieldsCount = input.readUnsignedShort().toUShort()
            val fields = List(fieldsCount.toInt()) { FieldInfo.fromDataInput(input, constantPoolMap) }
            val methodsCount = input.readUnsignedShort().toUShort()
            val methods = List(methodsCount.toInt()) { MethodInfo.fromDataInput(input, constantPoolMap) }
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolMap) }
            return ClassFile(
                origin,
                magic,
                minorVersion,
                majorVersion,
                constantPoolCount,
                constantPool,
                constantPoolMap,
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
            var index: UShort = 1u
            val constantPool = mutableListOf<ConstantInfo>()
            while (index < count) {
                val constantInfo = ConstantInfoFactory.fromDataInput(index, input)
                constantPool.add(constantInfo)
                index = (index + constantInfo.entriesTaken.toUShort()).toUShort()
            }
            return constantPool
        }
    }
}
