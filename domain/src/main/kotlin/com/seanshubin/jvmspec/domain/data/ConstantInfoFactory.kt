package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

object ConstantInfoFactory {
    fun fromDataInput(input: DataInput): ConstantInfo {
        val tag = input.readByte()
        val factory = factoryMap[tag]
            ?: throw IllegalArgumentException("Unknown constant tag: $tag")
        return factory(tag, input)
    }

    val factoryMap: Map<Byte, (Byte, DataInput) -> ConstantInfo> = mapOf(
        ConstantUtf8Info.TAG to ConstantUtf8Info::fromDataInput,
        ConstantIntegerInfo.TAG to ConstantIntegerInfo::fromDataInput,
        ConstantFloatInfo.TAG to ConstantFloatInfo::fromDataInput,
        ConstantLongInfo.TAG to ConstantLongInfo::fromDataInput,
        ConstantDoubleInfo.TAG to ConstantDoubleInfo::fromDataInput,
        ConstantClassInfo.TAG to ConstantClassInfo::fromDataInput,
        ConstantStringInfo.TAG to ConstantStringInfo::fromDataInput,
        ConstantFieldRefInfo.TAG to ConstantFieldRefInfo::fromDataInput,
        ConstantMethodRefInfo.TAG to ConstantMethodRefInfo::fromDataInput,
        ConstantInterfaceMethodRefInfo.TAG to ConstantInterfaceMethodRefInfo::fromDataInput,
        ConstantNameAndTypeInfo.TAG to ConstantNameAndTypeInfo::fromDataInput,
        ConstantMethodHandleInfo.TAG to ConstantMethodHandleInfo::fromDataInput,
        ConstantMethodTypeInfo.TAG to ConstantMethodTypeInfo::fromDataInput,
        ConstantDynamicInfo.TAG to ConstantDynamicInfo::fromDataInput,
        ConstantInvokeDynamicInfo.TAG to ConstantInvokeDynamicInfo::fromDataInput,
        ConstantModuleInfo.TAG to ConstantModuleInfo::fromDataInput,
        ConstantPackageInfo.TAG to ConstantPackageInfo::fromDataInput
    )
}
