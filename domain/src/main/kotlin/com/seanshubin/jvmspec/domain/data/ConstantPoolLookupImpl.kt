package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toSanitizedString

class ConstantPoolLookupImpl(val constantList: List<ConstantInfo>) : ConstantPoolLookup {
    private val constantMap = mutableMapOf<Int, ConstantInfo>()

    init {
        var index = 1
        constantList.forEach { constant ->
            constantMap[index] = constant
            index += constant.entriesTaken
        }
    }

    override fun lookupUtf8Value(constantIndex: UShort): String {
        val utf8 = constantMap.getValue(constantIndex.toInt()) as ConstantUtf8Info
        return utf8.utf8Value
    }

    override fun lookupClassNameValue(constantIndex: UShort): String {
        val classInfo = constantMap.getValue(constantIndex.toInt()) as ConstantClassInfo
        return lookupUtf8Value(classInfo.nameIndex)
    }

    override fun lines(): List<String> {
        return constantList.map { it.annotatedLine(this) }
    }

    override fun lookupUtf8(index: UShort): ConstantUtf8Info {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun lookupCLass(index: UShort): ConstantClassInfo {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun lookupNameAndType(index: UShort): ConstantNameAndTypeInfo {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun utf8Line(index: UShort): String {
        val utf8Info = constantMap.getValue(index.toInt()) as ConstantUtf8Info
        val utf8Value = utf8Info.utf8Value
        val sanitized = utf8Value.toSanitizedString()
        return "utf8($index)$sanitized"
    }

    override fun classLine(index: UShort): String {
        val classInfo = constantMap.getValue(index.toInt()) as ConstantClassInfo
        val utf8Index = classInfo.nameIndex
        val utf8Line = utf8Line(utf8Index)
        return "class($index)->$utf8Line"
    }

    override fun nameAndTypeLine(index: UShort): String {
        val nameAndTypeInfo = constantMap.getValue(index.toInt()) as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val nameLine = utf8Line(nameIndex)
        val descriptorLine = utf8Line(descriptorIndex)
        return "nameAndType($index)->($nameLine,$descriptorLine)"
    }

    override fun referenceIndexLine(index: UShort): String {
        val referenceInfo = constantMap.getValue(index.toInt()) as ConstantRefInfo
        val classIndex = referenceInfo.classIndex
        val nameAndTypeIndex = referenceInfo.nameAndTypeIndex
        val classLine = classLine(classIndex)
        val nameAndTypeLine = nameAndTypeLine(nameAndTypeIndex)
        return "reference($index)->($classLine,$nameAndTypeLine)"
    }
}
