package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiConstant
import com.seanshubin.jvmspec.domain.data.*
import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

object ApiConstantFactory {
    fun createByOffset(classFile: ClassFile, offset: Int): ApiConstant.Constant {
        val constantsByIndex: Map<Int, ConstantInfo> = classFile.constantPool.associateBy { it.index }
        val constantInfo = classFile.constantPool[offset]
        return createConstantMap.getValue(constantInfo.tag).invoke(constantsByIndex, constantInfo)
    }

    fun createByIndex(classFile: ClassFile, index: Int): ApiConstant.Constant {
        val constantsByIndex: Map<Int, ConstantInfo> = classFile.constantPool.associateBy { it.index }
        val constantInfo = constantsByIndex.getValue(index)
        return createConstantMap.getValue(constantInfo.tag).invoke(constantsByIndex, constantInfo)
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

    fun createUtf8(constantsByIndex: Map<Int, ConstantInfo>, constantInfo: ConstantInfo): ApiConstant.Constant {
        constantInfo as ConstantUtf8Info
        val value = ApiConstant.StringValue(constantInfo.utf8Value)
        return ApiConstant.Constant(
            constantInfo.index,
            constantInfo.tag.id.toInt(),
            constantInfo.tag.name,
            listOf(value)
        )
    }

    fun createClass(constantsByIndex: Map<Int, ConstantInfo>, classConstantInfo: ConstantInfo): ApiConstant.Constant {
        classConstantInfo as ConstantClassInfo
        val nameIndex = classConstantInfo.nameIndex
        val nameConstantInfo = constantsByIndex.getValue(nameIndex.toInt())
        val name = createUtf8(constantsByIndex, nameConstantInfo)
        return ApiConstant.Constant(
            classConstantInfo.index,
            classConstantInfo.tag.id.toInt(),
            classConstantInfo.tag.name,
            listOf(name)
        )
    }

    fun createNameAndType(
        constantsByIndex: Map<Int, ConstantInfo>,
        nameAndTypeInfo: ConstantInfo
    ): ApiConstant.Constant {
        nameAndTypeInfo as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val nameConstantInfo = constantsByIndex.getValue(nameIndex.toInt())
        val name = createUtf8(constantsByIndex, nameConstantInfo)
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantsByIndex.getValue(descriptorIndex.toInt())
        val descriptor = createUtf8(constantsByIndex, descriptorConstantInfo)
        return ApiConstant.Constant(
            nameAndTypeInfo.index,
            nameAndTypeInfo.tag.id.toInt(),
            nameAndTypeInfo.tag.name,
            listOf(name, descriptor)
        )
    }

    fun createRef(constantsByIndex: Map<Int, ConstantInfo>, refInfo: ConstantInfo): ApiConstant.Constant {
        refInfo as ConstantRefInfo
        val classIndex = refInfo.classIndex
        val classConstantInfo = constantsByIndex.getValue(classIndex.toInt())
        val className = createClass(constantsByIndex, classConstantInfo)
        val nameAndTypeIndex = refInfo.nameAndTypeIndex
        val nameAndTypeInfo = constantsByIndex.getValue(nameAndTypeIndex.toInt())
        val nameAndType = createNameAndType(constantsByIndex, nameAndTypeInfo)
        return ApiConstant.Constant(
            refInfo.index,
            refInfo.tag.id.toInt(),
            refInfo.tag.name,
            listOf(className, nameAndType)
        )
    }

    fun createString(constantsByIndex: Map<Int, ConstantInfo>, stringInfo: ConstantInfo): ApiConstant.Constant {
        stringInfo as ConstantStringInfo
        val stringIndex = stringInfo.stringIndex
        val stringConstantInfo = constantsByIndex.getValue(stringIndex.toInt())
        val string = createUtf8(constantsByIndex, stringConstantInfo)
        return ApiConstant.Constant(
            stringInfo.index,
            stringInfo.tag.id.toInt(),
            stringInfo.tag.name,
            listOf(string)
        )
    }

    fun createInteger(constantsByIndex: Map<Int, ConstantInfo>, integerInfo: ConstantInfo): ApiConstant.Constant {
        integerInfo as ConstantIntegerInfo
        val integerValue = ApiConstant.IntegerValue(integerInfo.intValue)
        return ApiConstant.Constant(
            integerInfo.index,
            integerInfo.tag.id.toInt(),
            integerInfo.tag.name,
            listOf(integerValue)
        )
    }

    fun createMethodHandle(
        constantsByIndex: Map<Int, ConstantInfo>,
        methodHandleInfo: ConstantInfo
    ): ApiConstant.Constant {
        methodHandleInfo as ConstantMethodHandleInfo
        val referenceKind = methodHandleInfo.referenceKind
        val referenceKindValue = ApiConstant.ReferenceKindValue(referenceKind.code.toInt(), referenceKind.name)
        val refIndex = methodHandleInfo.referenceIndex
        val refConstantInfo = constantsByIndex.getValue(refIndex.toInt())
        val ref = createRef(constantsByIndex, refConstantInfo)
        return ApiConstant.Constant(
            methodHandleInfo.index,
            methodHandleInfo.tag.id.toInt(),
            methodHandleInfo.tag.name,
            listOf(referenceKindValue, ref)
        )
    }

    fun createInvokeDynamic(
        constantsByIndex: Map<Int, ConstantInfo>,
        invokeDynamicInfo: ConstantInfo
    ): ApiConstant.Constant {
        invokeDynamicInfo as ConstantInvokeDynamicInfo
        val bootstrapMethodAttrIndex = invokeDynamicInfo.bootstrapMethodAttrIndex
        val bootstrapMethodAttr = ApiConstant.IndexValue(bootstrapMethodAttrIndex.toInt())
        val nameAndTypeIndex = invokeDynamicInfo.nameAndTypeIndex
        val nameAndType = createNameAndType(constantsByIndex, constantsByIndex.getValue(nameAndTypeIndex.toInt()))
        return ApiConstant.Constant(
            invokeDynamicInfo.index,
            invokeDynamicInfo.tag.id.toInt(),
            invokeDynamicInfo.tag.name,
            listOf(bootstrapMethodAttr, nameAndType)
        )
    }

    fun createLong(constantsByIndex: Map<Int, ConstantInfo>, longInfo: ConstantInfo): ApiConstant.Constant {
        longInfo as ConstantLongInfo
        val longValue = ApiConstant.LongValue(longInfo.longValue)
        return ApiConstant.Constant(
            longInfo.index,
            longInfo.tag.id.toInt(),
            longInfo.tag.name,
            listOf(longValue)
        )
    }

    fun createMethodType(constantsByIndex: Map<Int, ConstantInfo>, methodTypeInfo: ConstantInfo): ApiConstant.Constant {
        methodTypeInfo as ConstantMethodTypeInfo
        val descriptorIndex = methodTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantsByIndex.getValue(descriptorIndex.toInt())
        val descriptor = createUtf8(constantsByIndex, descriptorConstantInfo)
        return ApiConstant.Constant(
            methodTypeInfo.index,
            methodTypeInfo.tag.id.toInt(),
            methodTypeInfo.tag.name,
            listOf(descriptor)
        )
    }

    fun createModule(constantsByIndex: Map<Int, ConstantInfo>, moduleInfo: ConstantInfo): ApiConstant.Constant {
        moduleInfo as ConstantModuleInfo
        val nameIndex = moduleInfo.nameIndex
        val nameConstantInfo = constantsByIndex.getValue(nameIndex.toInt())
        val name = createUtf8(constantsByIndex, nameConstantInfo)
        return ApiConstant.Constant(
            moduleInfo.index,
            moduleInfo.tag.id.toInt(),
            moduleInfo.tag.name,
            listOf(name)
        )
    }

    fun createPackage(constantsByIndex: Map<Int, ConstantInfo>, packageInfo: ConstantInfo): ApiConstant.Constant {
        packageInfo as ConstantPackageInfo
        val nameIndex = packageInfo.nameIndex
        val nameConstantInfo = constantsByIndex.getValue(nameIndex.toInt())
        val name = createUtf8(constantsByIndex, nameConstantInfo)
        return ApiConstant.Constant(
            packageInfo.index,
            packageInfo.tag.id.toInt(),
            packageInfo.tag.name,
            listOf(name)
        )
    }

    fun createDouble(constantsByIndex: Map<Int, ConstantInfo>, doubleInfo: ConstantInfo): ApiConstant.Constant {
        doubleInfo as ConstantDoubleInfo
        val doubleValue = ApiConstant.DoubleValue(doubleInfo.doubleValue)
        return ApiConstant.Constant(
            doubleInfo.index,
            doubleInfo.tag.id.toInt(),
            doubleInfo.tag.name,
            listOf(doubleValue)
        )
    }

    fun createFloat(constantsByIndex: Map<Int, ConstantInfo>, floatInfo: ConstantInfo): ApiConstant.Constant {
        floatInfo as ConstantFloatInfo
        val floatValue = ApiConstant.FloatValue(floatInfo.floatValue)
        return ApiConstant.Constant(
            floatInfo.index,
            floatInfo.tag.id.toInt(),
            floatInfo.tag.name,
            listOf(floatValue)
        )
    }
}
