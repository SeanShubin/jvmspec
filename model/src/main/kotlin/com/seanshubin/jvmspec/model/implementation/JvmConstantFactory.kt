package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag
import com.seanshubin.jvmspec.classfile.structure.*
import com.seanshubin.jvmspec.model.api.JvmConstant

object JvmConstantFactory {
    fun createByIndex(constantPoolMap: Map<UShort, ConstantInfo>, index: UShort): JvmConstant {
        val constantInfo = constantPoolMap.getValue(index)
        return createConstantMap.getValue(constantInfo.tag).invoke(constantPoolMap, constantInfo)
    }

    private val createConstantMap: Map<ConstantPoolTag, (Map<UShort, ConstantInfo>, ConstantInfo) -> JvmConstant> =
        mapOf(
            ConstantPoolTag.UTF8 to ::createUtf8,
            ConstantPoolTag.CLASS to ::createClass,
            ConstantPoolTag.NAME_AND_TYPE to ::createNameAndType,
            ConstantPoolTag.METHOD_REF to ::createRef,
            ConstantPoolTag.INTERFACE_METHOD_REF to ::createRef,
            ConstantPoolTag.FIELD_REF to ::createRef,
            ConstantPoolTag.STRING to ::createString,
            ConstantPoolTag.INTEGER to ::createInteger,
            ConstantPoolTag.FLOAT to ::createFloat,
            ConstantPoolTag.LONG to ::createLong,
            ConstantPoolTag.DOUBLE to ::createDouble,
            ConstantPoolTag.METHOD_TYPE to ::createMethodType,
            ConstantPoolTag.METHOD_HANDLE to ::createMethodHandle,
            ConstantPoolTag.DYNAMIC to ::createDynamic,
            ConstantPoolTag.INVOKE_DYNAMIC to ::createInvokeDynamic,
            ConstantPoolTag.MODULE to ::createModule,
            ConstantPoolTag.PACKAGE to ::createPackage
        )

    private fun createUtf8(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantUtf8Info
        return JvmConstant.Utf8(
            tag = constantInfo.tag,
            value = constantInfo.utf8Value
        )
    }

    private fun createClass(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantClassInfo
        val name = createByIndex(constantPoolMap, constantInfo.nameIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.Class(
            tag = constantInfo.tag,
            nameUtf8 = name
        )
    }

    private fun createNameAndType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        constantInfo: ConstantInfo
    ): JvmConstant {
        constantInfo as ConstantNameAndTypeInfo
        val name = createByIndex(constantPoolMap, constantInfo.nameIndex) as JvmConstant.JvmConstantUtf8
        val descriptor = createByIndex(constantPoolMap, constantInfo.descriptorIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.NameAndType(
            tag = constantInfo.tag,
            nameUtf8 = name,
            descriptorUtf8 = descriptor
        )
    }

    private fun createRef(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantRefInfo
        val jvmClass = createByIndex(constantPoolMap, constantInfo.classIndex) as JvmConstant.JvmConstantClass
        val jvmNameAndType =
            createByIndex(constantPoolMap, constantInfo.nameAndTypeIndex) as JvmConstant.JvmConstantNameAndType
        return JvmConstant.Ref(
            tag = constantInfo.tag,
            jvmClass = jvmClass,
            jvmNameAndType = jvmNameAndType
        )
    }

    private fun createString(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantStringInfo
        val value = createByIndex(constantPoolMap, constantInfo.stringIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.StringConstant(
            tag = constantInfo.tag,
            valueUtf8 = value
        )
    }

    private fun createInteger(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantIntegerInfo
        return JvmConstant.IntegerConstant(
            tag = constantInfo.tag,
            value = constantInfo.intValue
        )
    }

    private fun createFloat(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantFloatInfo
        return JvmConstant.FloatConstant(
            tag = constantInfo.tag,
            value = constantInfo.floatValue
        )
    }

    private fun createLong(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantLongInfo
        return JvmConstant.LongConstant(
            tag = constantInfo.tag,
            value = constantInfo.longValue
        )
    }

    private fun createDouble(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantDoubleInfo
        return JvmConstant.DoubleConstant(
            tag = constantInfo.tag,
            value = constantInfo.doubleValue
        )
    }

    private fun createMethodType(
        constantPoolMap: Map<UShort, ConstantInfo>,
        constantInfo: ConstantInfo
    ): JvmConstant {
        constantInfo as ConstantMethodTypeInfo
        val descriptor = createByIndex(constantPoolMap, constantInfo.descriptorIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.MethodType(
            tag = constantInfo.tag,
            descriptorUtf8 = descriptor
        )
    }

    private fun createMethodHandle(
        constantPoolMap: Map<UShort, ConstantInfo>,
        constantInfo: ConstantInfo
    ): JvmConstant {
        constantInfo as ConstantMethodHandleInfo
        val reference = createByIndex(constantPoolMap, constantInfo.referenceIndex) as JvmConstant.JvmConstantRef
        return JvmConstant.MethodHandle(
            tag = constantInfo.tag,
            referenceKind = constantInfo.referenceKind,
            reference = reference
        )
    }

    private fun createDynamic(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantDynamicInfo
        val nameAndType =
            createByIndex(constantPoolMap, constantInfo.nameAndTypeIndex) as JvmConstant.JvmConstantNameAndType
        return JvmConstant.Dynamic(
            tag = constantInfo.tag,
            bootstrapMethodAttrIndex = constantInfo.bootstrapMethodAttrIndex.toInt(),
            nameAndType = nameAndType
        )
    }

    private fun createInvokeDynamic(
        constantPoolMap: Map<UShort, ConstantInfo>,
        constantInfo: ConstantInfo
    ): JvmConstant {
        constantInfo as ConstantInvokeDynamicInfo
        val nameAndType =
            createByIndex(constantPoolMap, constantInfo.nameAndTypeIndex) as JvmConstant.JvmConstantNameAndType
        return JvmConstant.Dynamic(
            tag = constantInfo.tag,
            bootstrapMethodAttrIndex = constantInfo.bootstrapMethodAttrIndex.toInt(),
            nameAndType = nameAndType
        )
    }

    private fun createModule(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantModuleInfo
        val moduleName = createByIndex(constantPoolMap, constantInfo.nameIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.Module(
            tag = constantInfo.tag,
            moduleNameUtf8 = moduleName
        )
    }

    private fun createPackage(constantPoolMap: Map<UShort, ConstantInfo>, constantInfo: ConstantInfo): JvmConstant {
        constantInfo as ConstantPackageInfo
        val packageName = createByIndex(constantPoolMap, constantInfo.nameIndex) as JvmConstant.JvmConstantUtf8
        return JvmConstant.Package(
            tag = constantInfo.tag,
            packageNameUtf8 = packageName
        )
    }
}
