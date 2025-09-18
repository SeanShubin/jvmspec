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

    override fun className(index: UShort): String {
        val classInfo = constantMap.getValue(index.toInt()) as ConstantClassInfo
        val utf8Index = classInfo.nameIndex
        val utf8Value = lookupUtf8Value(utf8Index)
        return utf8Value
    }

    override fun ref(index: UShort): Triple<String, String, String> {
        val methodRefInfo = constantMap.getValue(index.toInt()) as ConstantRefInfo
        val classIndex = methodRefInfo.classIndex
        val nameAndTypeIndex = methodRefInfo.nameAndTypeIndex
        val className = className(classIndex)
        val methodNameAndType = nameAndType(nameAndTypeIndex)
        val methodName = methodNameAndType.first
        val methodDescriptor = methodNameAndType.second
        return Triple(className, methodName, methodDescriptor)
    }

    override fun nameAndType(index: UShort): Pair<String, String> {
        val nameAndTypeInfo = constantMap.getValue(index.toInt()) as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val name = lookupUtf8Value(nameIndex)
        val descriptor = lookupUtf8Value(descriptorIndex)
        return Pair(name, descriptor)
    }

    override fun lines(): List<String> {
        return constantList.map { it.annotatedLine(this) }
    }

    override fun line(index: UShort): String {
        if (index.toInt() == 0) return "<none>"
        val info = constantMap.getValue(index.toInt())
        return info.annotatedLine(this)
    }

    override fun utf8Line(index: UShort): String {
        val utf8Info = constantMap.getValue(index.toInt()) as ConstantUtf8Info
        val utf8Value = utf8Info.utf8Value
        val sanitized = utf8Value.toSanitizedString()
        return "const[$index]:Utf8=$sanitized"
    }

    override fun classLine(index: UShort): String {
        if (index.toInt() == 0) return "<none>"
        val classInfo = constantMap.getValue(index.toInt()) as ConstantClassInfo
        val utf8Index = classInfo.nameIndex
        val utf8Line = utf8Line(utf8Index)
        return "const[$index]:Class=$utf8Line"
    }

    override fun nameAndTypeLine(index: UShort): String {
        val nameAndTypeInfo = constantMap.getValue(index.toInt()) as ConstantNameAndTypeInfo
        val nameIndex = nameAndTypeInfo.nameIndex
        val descriptorIndex = nameAndTypeInfo.descriptorIndex
        val nameLine = utf8Line(nameIndex)
        val descriptorLine = utf8Line(descriptorIndex)
        return "const[$index]:NameAndType=($nameLine,$descriptorLine)"
    }

    override fun referenceIndexLine(index: UShort): String {
        val referenceInfo = constantMap.getValue(index.toInt()) as ConstantRefInfo
        val classIndex = referenceInfo.classIndex
        val nameAndTypeIndex = referenceInfo.nameAndTypeIndex
        val classLine = classLine(classIndex)
        val nameAndTypeLine = nameAndTypeLine(nameAndTypeIndex)
        return "const[$index]:Reference=($classLine,$nameAndTypeLine)"
    }
}
