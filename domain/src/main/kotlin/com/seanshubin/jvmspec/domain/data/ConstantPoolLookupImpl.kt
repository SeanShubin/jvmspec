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

    override fun getUtf8(constantIndex: Short): String {
        val utf8 = constantMap.getValue(constantIndex.toInt()) as ConstantUtf8Info
        return String(utf8.bytes.toByteArray(), Charsets.UTF_8)
    }
}