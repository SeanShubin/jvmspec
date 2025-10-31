package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiConstant
import com.seanshubin.jvmspec.domain.data.*
import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

object ApiConstantFactory {
    fun createByIndex(constantPoolMap: Map<UShort, ConstantInfo>, index: UShort): ApiConstant.Constant {
        val constantInfo = constantPoolMap.getValue(index)
        return createConstantMap.getValue(constantInfo.tag).invoke(constantPoolMap, constantInfo)
    }

    val createConstantMap = mapOf(
        ConstantPoolTag.UTF8 to ::createUtf8,
        ConstantPoolTag.CLASS to ::createClass,
        ConstantPoolTag.NAME_AND_TYPE to ::createNameAndType,
        ConstantPoolTag.METHOD_REF to ::createRef,
        ConstantPoolTag.INTERFACE_METHOD_REF to ::createRef,
        ConstantPoolTag.FIELD_REF to ::createRef,
        ConstantPoolTag.STRING to ::createString,
        ConstantPoolTag.INTEGER to ::createInteger,
        ConstantPoolTag.FLOAT to ::createFloat,
        ConstantPoolTag.METHOD_TYPE to ::createMethodType,
        ConstantPoolTag.METHOD_HANDLE to ::createMethodHandle,
        ConstantPoolTag.INVOKE_DYNAMIC to ::createInvokeDynamic,
        ConstantPoolTag.LONG to ::createLong,
        ConstantPoolTag.DOUBLE to ::createDouble,
        ConstantPoolTag.MODULE to ::createModule,
        ConstantPoolTag.PACKAGE to ::createPackage
    )

    fun createUtf8(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): ApiConstant.Constant {
        constantInfo as ConstantUtf8Info
        val value = ApiConstant.StringValue(constantInfo.utf8Value)
        return ApiConstant.Constant(
            constantInfo.index,
            constantInfo.tag.id,
            constantInfo.tag.name,
            listOf(value)
        )
    }

    fun createClass(constantPoolMap: Map<UShort, ConstantInfo>, classConstantInfo: ConstantInfo): ApiConstant.Constant {
        classConstantInfo as ConstantClassInfo
        val nameIndex = classConstantInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return ApiConstant.Constant(
            classConstantInfo.index,
            classConstantInfo.tag.id,
            classConstantInfo.tag.name,
            listOf(name)
        )
    }

    fun createNameAndType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        nameAndTypeInfo: ConstantInfo
    ): ApiConstant.Constant {
        nameAndTypeInfo as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantPoolMap.getValue(descriptorIndex)
        val descriptor = createUtf8(constantPoolMap, descriptorConstantInfo)
        return ApiConstant.Constant(
            nameAndTypeInfo.index,
            nameAndTypeInfo.tag.id,
            nameAndTypeInfo.tag.name,
            listOf(name, descriptor)
        )
    }

    fun createRef(constantPoolMap: Map<UShort, ConstantInfo>, refInfo: ConstantInfo): ApiConstant.Constant {
        refInfo as ConstantRefInfo
        val classIndex = refInfo.classIndex
        val classConstantInfo = constantPoolMap.getValue(classIndex)
        val className = createClass(constantPoolMap, classConstantInfo)
        val nameAndTypeIndex = refInfo.nameAndTypeIndex
        val nameAndTypeInfo = constantPoolMap.getValue(nameAndTypeIndex)
        val nameAndType = createNameAndType(constantPoolMap, nameAndTypeInfo)
        return ApiConstant.Constant(
            refInfo.index,
            refInfo.tag.id,
            refInfo.tag.name,
            listOf(className, nameAndType)
        )
    }

    fun createString(constantPoolMap: Map<UShort, ConstantInfo>, stringInfo: ConstantInfo): ApiConstant.Constant {
        stringInfo as ConstantStringInfo
        val stringIndex = stringInfo.stringIndex
        val stringConstantInfo = constantPoolMap.getValue(stringIndex)
        val string = createUtf8(constantPoolMap, stringConstantInfo)
        return ApiConstant.Constant(
            stringInfo.index,
            stringInfo.tag.id,
            stringInfo.tag.name,
            listOf(string)
        )
    }

    fun createInteger(constantPoolMap: Map<UShort, ConstantInfo>, integerInfo: ConstantInfo): ApiConstant.Constant {
        integerInfo as ConstantIntegerInfo
        val integerValue = ApiConstant.IntegerValue(integerInfo.intValue)
        return ApiConstant.Constant(
            integerInfo.index,
            integerInfo.tag.id,
            integerInfo.tag.name,
            listOf(integerValue)
        )
    }

    fun createMethodHandle(
        constantPoolMap: Map<UShort, ConstantInfo>,
        methodHandleInfo: ConstantInfo
    ): ApiConstant.Constant {
        methodHandleInfo as ConstantMethodHandleInfo
        val referenceKind = methodHandleInfo.referenceKind
        val referenceKindValue = ApiConstant.ReferenceKindValue(referenceKind.code, referenceKind.name)
        val refIndex = methodHandleInfo.referenceIndex
        val refConstantInfo = constantPoolMap.getValue(refIndex)
        val ref = createRef(constantPoolMap, refConstantInfo)
        return ApiConstant.Constant(
            methodHandleInfo.index,
            methodHandleInfo.tag.id,
            methodHandleInfo.tag.name,
            listOf(referenceKindValue, ref)
        )
    }

    fun createInvokeDynamic(
        constantPoolMap: Map<UShort, ConstantInfo>,
        invokeDynamicInfo: ConstantInfo
    ): ApiConstant.Constant {
        invokeDynamicInfo as ConstantInvokeDynamicInfo
        val bootstrapMethodAttrIndex = invokeDynamicInfo.bootstrapMethodAttrIndex
        val bootstrapMethodAttr = ApiConstant.IndexValue(bootstrapMethodAttrIndex)
        val nameAndTypeIndex = invokeDynamicInfo.nameAndTypeIndex
        val nameAndType = createNameAndType(constantPoolMap, constantPoolMap.getValue(nameAndTypeIndex))
        return ApiConstant.Constant(
            invokeDynamicInfo.index,
            invokeDynamicInfo.tag.id,
            invokeDynamicInfo.tag.name,
            listOf(bootstrapMethodAttr, nameAndType)
        )
    }

    fun createLong(constantPoolMap: Map<UShort, ConstantInfo>, longInfo: ConstantInfo): ApiConstant.Constant {
        longInfo as ConstantLongInfo
        val longValue = ApiConstant.LongValue(longInfo.longValue)
        return ApiConstant.Constant(
            longInfo.index,
            longInfo.tag.id,
            longInfo.tag.name,
            listOf(longValue)
        )
    }

    fun createMethodType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        methodTypeInfo: ConstantInfo
    ): ApiConstant.Constant {
        methodTypeInfo as ConstantMethodTypeInfo
        val descriptorIndex = methodTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantPoolMap.getValue(descriptorIndex)
        val descriptor = createUtf8(constantPoolMap, descriptorConstantInfo)
        return ApiConstant.Constant(
            methodTypeInfo.index,
            methodTypeInfo.tag.id,
            methodTypeInfo.tag.name,
            listOf(descriptor)
        )
    }

    fun createModule(constantPoolMap: Map<UShort, ConstantInfo>, moduleInfo: ConstantInfo): ApiConstant.Constant {
        moduleInfo as ConstantModuleInfo
        val nameIndex = moduleInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return ApiConstant.Constant(
            moduleInfo.index,
            moduleInfo.tag.id,
            moduleInfo.tag.name,
            listOf(name)
        )
    }

    fun createPackage(constantPoolMap: Map<UShort, ConstantInfo>, packageInfo: ConstantInfo): ApiConstant.Constant {
        packageInfo as ConstantPackageInfo
        val nameIndex = packageInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return ApiConstant.Constant(
            packageInfo.index,
            packageInfo.tag.id,
            packageInfo.tag.name,
            listOf(name)
        )
    }

    fun createDouble(constantPoolMap: Map<UShort, ConstantInfo>, doubleInfo: ConstantInfo): ApiConstant.Constant {
        doubleInfo as ConstantDoubleInfo
        val doubleValue = ApiConstant.DoubleValue(doubleInfo.doubleValue)
        return ApiConstant.Constant(
            doubleInfo.index,
            doubleInfo.tag.id,
            doubleInfo.tag.name,
            listOf(doubleValue)
        )
    }

    fun createFloat(constantPoolMap: Map<UShort, ConstantInfo>, floatInfo: ConstantInfo): ApiConstant.Constant {
        floatInfo as ConstantFloatInfo
        val floatValue = ApiConstant.FloatValue(floatInfo.floatValue)
        return ApiConstant.Constant(
            floatInfo.index,
            floatInfo.tag.id,
            floatInfo.tag.name,
            listOf(floatValue)
        )
    }
}
