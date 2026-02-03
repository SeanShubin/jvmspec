package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeModuleInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val moduleNameIndex: UShort,
    val moduleFlags: Int,
    val moduleVersionIndex: UShort,
    val requiresCount: UShort,
    val requires: List<ModuleRequires>,
    val exportsCount: UShort,
    val exports: List<ModuleExports>,
    val opensCount: UShort,
    val opens: List<ModuleOpens>,
    val usesCount: UShort,
    val usesIndex: List<UShort>,
    val providesCount: UShort,
    val provides: List<ModuleProvides>
) : AttributeInfo {
    companion object {
        const val NAME = "Module"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeModuleInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))

            val moduleNameIndex = input.readUnsignedShort().toUShort()
            val moduleFlags = input.readUnsignedShort()
            val moduleVersionIndex = input.readUnsignedShort().toUShort()

            val requiresCount = input.readUnsignedShort().toUShort()
            val requires = (0 until requiresCount.toInt()).map {
                ModuleRequires.fromDataInput(input)
            }

            val exportsCount = input.readUnsignedShort().toUShort()
            val exports = (0 until exportsCount.toInt()).map {
                ModuleExports.fromDataInput(input)
            }

            val opensCount = input.readUnsignedShort().toUShort()
            val opens = (0 until opensCount.toInt()).map {
                ModuleOpens.fromDataInput(input)
            }

            val usesCount = input.readUnsignedShort().toUShort()
            val usesIndex = (0 until usesCount.toInt()).map {
                input.readUnsignedShort().toUShort()
            }

            val providesCount = input.readUnsignedShort().toUShort()
            val provides = (0 until providesCount.toInt()).map {
                ModuleProvides.fromDataInput(input)
            }

            return AttributeModuleInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                moduleNameIndex,
                moduleFlags,
                moduleVersionIndex,
                requiresCount,
                requires,
                exportsCount,
                exports,
                opensCount,
                opens,
                usesCount,
                usesIndex,
                providesCount,
                provides
            )
        }
    }
}
