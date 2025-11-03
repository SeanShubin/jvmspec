package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.*
import com.seanshubin.jvmspec.domain.jvm.JvmConstant
import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

object JvmConstantFactory {
    fun createByIndex(constantPoolMap: Map<UShort, ConstantInfo>, index: UShort): JvmConstant.Constant {
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

    fun createUtf8(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant.Constant {
        constantInfo as ConstantUtf8Info
        val value = JvmConstant.StringValue(constantInfo.utf8Value)
        return JvmConstant.Constant(
            constantInfo.index,
            constantInfo.tag,
            listOf(value)
        )
    }

    fun createClass(constantPoolMap: Map<UShort, ConstantInfo>, classConstantInfo: ConstantInfo): JvmConstant.Constant {
        classConstantInfo as ConstantClassInfo
        val nameIndex = classConstantInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return JvmConstant.Constant(
            classConstantInfo.index,
            classConstantInfo.tag,
            listOf(name)
        )
    }

    fun createNameAndType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        nameAndTypeInfo: ConstantInfo
    ): JvmConstant.Constant {
        nameAndTypeInfo as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantPoolMap.getValue(descriptorIndex)
        val descriptor = createUtf8(constantPoolMap, descriptorConstantInfo)
        return JvmConstant.Constant(
            nameAndTypeInfo.index,
            nameAndTypeInfo.tag,
            listOf(name, descriptor)
        )
    }

    fun createRef(constantPoolMap: Map<UShort, ConstantInfo>, refInfo: ConstantInfo): JvmConstant.Constant {
        refInfo as ConstantRefInfo
        val classIndex = refInfo.classIndex
        val classConstantInfo = constantPoolMap.getValue(classIndex)
        val className = createClass(constantPoolMap, classConstantInfo)
        val nameAndTypeIndex = refInfo.nameAndTypeIndex
        val nameAndTypeInfo = constantPoolMap.getValue(nameAndTypeIndex)
        val nameAndType = createNameAndType(constantPoolMap, nameAndTypeInfo)
        return JvmConstant.Constant(
            refInfo.index,
            refInfo.tag,
            listOf(className, nameAndType)
        )
    }

    fun createString(constantPoolMap: Map<UShort, ConstantInfo>, stringInfo: ConstantInfo): JvmConstant.Constant {
        stringInfo as ConstantStringInfo
        val stringIndex = stringInfo.stringIndex
        val stringConstantInfo = constantPoolMap.getValue(stringIndex)
        val string = createUtf8(constantPoolMap, stringConstantInfo)
        return JvmConstant.Constant(
            stringInfo.index,
            stringInfo.tag,
            listOf(string)
        )
    }

    fun createInteger(constantPoolMap: Map<UShort, ConstantInfo>, integerInfo: ConstantInfo): JvmConstant.Constant {
        integerInfo as ConstantIntegerInfo
        val integerValue = JvmConstant.IntegerValue(integerInfo.intValue)
        return JvmConstant.Constant(
            integerInfo.index,
            integerInfo.tag,
            listOf(integerValue)
        )
    }

    fun createMethodHandle(
        constantPoolMap: Map<UShort, ConstantInfo>,
        methodHandleInfo: ConstantInfo
    ): JvmConstant.Constant {
        methodHandleInfo as ConstantMethodHandleInfo
        val referenceKind = methodHandleInfo.referenceKind
        val referenceKindValue = JvmConstant.ReferenceKindValue(referenceKind.code, referenceKind.name)
        val refIndex = methodHandleInfo.referenceIndex
        val refConstantInfo = constantPoolMap.getValue(refIndex)
        val ref = createRef(constantPoolMap, refConstantInfo)
        return JvmConstant.Constant(
            methodHandleInfo.index,
            methodHandleInfo.tag,
            listOf(referenceKindValue, ref)
        )
    }

    fun createInvokeDynamic(
        constantPoolMap: Map<UShort, ConstantInfo>,
        invokeDynamicInfo: ConstantInfo
    ): JvmConstant.Constant {
        invokeDynamicInfo as ConstantInvokeDynamicInfo
        val bootstrapMethodAttrIndex = invokeDynamicInfo.bootstrapMethodAttrIndex
        val bootstrapMethodAttr = JvmConstant.IndexValue(bootstrapMethodAttrIndex)
        val nameAndTypeIndex = invokeDynamicInfo.nameAndTypeIndex
        val nameAndType = createNameAndType(constantPoolMap, constantPoolMap.getValue(nameAndTypeIndex))
        return JvmConstant.Constant(
            invokeDynamicInfo.index,
            invokeDynamicInfo.tag,
            listOf(bootstrapMethodAttr, nameAndType)
        )
    }

    fun createLong(constantPoolMap: Map<UShort, ConstantInfo>, longInfo: ConstantInfo): JvmConstant.Constant {
        longInfo as ConstantLongInfo
        val longValue = JvmConstant.LongValue(longInfo.longValue)
        return JvmConstant.Constant(
            longInfo.index,
            longInfo.tag,
            listOf(longValue)
        )
    }

    fun createMethodType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        methodTypeInfo: ConstantInfo
    ): JvmConstant.Constant {
        methodTypeInfo as ConstantMethodTypeInfo
        val descriptorIndex = methodTypeInfo.descriptorIndex
        val descriptorConstantInfo = constantPoolMap.getValue(descriptorIndex)
        val descriptor = createUtf8(constantPoolMap, descriptorConstantInfo)
        return JvmConstant.Constant(
            methodTypeInfo.index,
            methodTypeInfo.tag,
            listOf(descriptor)
        )
    }

    fun createModule(constantPoolMap: Map<UShort, ConstantInfo>, moduleInfo: ConstantInfo): JvmConstant.Constant {
        moduleInfo as ConstantModuleInfo
        val nameIndex = moduleInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return JvmConstant.Constant(
            moduleInfo.index,
            moduleInfo.tag,
            listOf(name)
        )
    }

    fun createPackage(constantPoolMap: Map<UShort, ConstantInfo>, packageInfo: ConstantInfo): JvmConstant.Constant {
        packageInfo as ConstantPackageInfo
        val nameIndex = packageInfo.nameIndex
        val nameConstantInfo = constantPoolMap.getValue(nameIndex)
        val name = createUtf8(constantPoolMap, nameConstantInfo)
        return JvmConstant.Constant(
            packageInfo.index,
            packageInfo.tag,
            listOf(name)
        )
    }

    fun createDouble(constantPoolMap: Map<UShort, ConstantInfo>, doubleInfo: ConstantInfo): JvmConstant.Constant {
        doubleInfo as ConstantDoubleInfo
        val doubleValue = JvmConstant.DoubleValue(doubleInfo.doubleValue)
        return JvmConstant.Constant(
            doubleInfo.index,
            doubleInfo.tag,
            listOf(doubleValue)
        )
    }

    fun createFloat(constantPoolMap: Map<UShort, ConstantInfo>, floatInfo: ConstantInfo): JvmConstant.Constant {
        floatInfo as ConstantFloatInfo
        val floatValue = JvmConstant.FloatValue(floatInfo.floatValue)
        return JvmConstant.Constant(
            floatInfo.index,
            floatInfo.tag,
            listOf(floatValue)
        )
    }
}
