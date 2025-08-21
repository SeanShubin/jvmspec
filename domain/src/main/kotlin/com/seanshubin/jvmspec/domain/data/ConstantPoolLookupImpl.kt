package com.seanshubin.jvmspec.domain.data

class ConstantPoolLookupImpl(constantList: List<ConstantInfo>) : ConstantPoolLookup {
    private val constantMap = mutableMapOf<Int, ConstantInfo>()

    init {
        var index = 1
        constantList.forEach { constant ->
            constantMap[index] = constant
            index += constant.entriesTaken
        }
    }

    override fun getUtf8(constantIndex: UShort): String {
        val utf8 = constantMap.getValue(constantIndex.toInt()) as ConstantUtf8Info
        return utf8.utf8Value
    }

    override fun getClassName(constantIndex: UShort): String {
        val classInfo = constantMap.getValue(constantIndex.toInt()) as ConstantClassInfo
        return getUtf8(classInfo.nameIndex)
    }
}
