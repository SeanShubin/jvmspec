package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeModuleHashesInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val algorithmIndex: UShort,
    val modulesCount: UShort,
    val modules: List<ModuleHash>
) : AttributeInfo {
    companion object {
        const val NAME = "ModuleHashes"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeModuleHashesInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val algorithmIndex = input.readUnsignedShort().toUShort()
            val modulesCount = input.readUnsignedShort().toUShort()
            val modules = (0 until modulesCount.toInt()).map {
                ModuleHash.fromDataInput(input)
            }
            return AttributeModuleHashesInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                algorithmIndex,
                modulesCount,
                modules
            )
        }
    }
}
