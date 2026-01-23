package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeModulePackagesInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val packageCount: UShort,
    val packageIndex: List<UShort>
) : AttributeInfo {
    companion object {
        const val NAME = "ModulePackages"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeModulePackagesInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val packageCount = input.readUnsignedShort().toUShort()
            val packageIndex = (0 until packageCount.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return AttributeModulePackagesInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                packageCount,
                packageIndex
            )
        }
    }
}
