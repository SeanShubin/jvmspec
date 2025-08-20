package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ClassFile(
    val magic: Int,
    val minorVersion: Short,
    val majorVersion: Short,
    val constantPoolCount: Short,
    val constantPool: List<ConstantInfo>,
    val accessFlags: Short,
    val thisClass: Short,
    val superClass: Short,
    val interfacesCount: Short,
    val interfaces: List<Short>,
    val fieldsCount: Short,
    val fields: List<FieldInfo>,
    val methodsCount: Short,
    val methods: List<MethodInfo>,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
    fun lines(): List<String> {
        val constantPoolLines = constantPool.map { it.line() }.map(indent)
        return listOf(
            "magic: $magic",
            "minorVersion: $minorVersion",
            "majorVersion: $majorVersion",
            "constantPoolCount: $constantPoolCount",
            "constantPool:",
            *constantPoolLines.toTypedArray(),
            "accessFlags: $accessFlags",
            "thisClass: $thisClass",
            "superClass: $superClass",
            "interfacesCount: $interfacesCount",
            "interfaces: ${interfaces.joinToString(", ")}",
            "fieldsCount: $fieldsCount",
            "fields: ${fields.joinToString("\n")}",
            "methodsCount: $methodsCount",
            "methods: ${methods.joinToString("\n")}",
            "attributesCount: $attributesCount",
            "attributes: ${attributes.joinToString("\n")}"
        )
    }

    companion object {
        val indent: (String) -> String = { it.padStart(it.length + 2) }
        fun fromDataInput(input: DataInput): ClassFile {
            val magic = input.readInt()
            val minorVersion = input.readShort()
            val majorVersion = input.readShort()
            val constantPoolCount = input.readShort()
            val constantPool = readConstantPool(input, constantPoolCount)
            val constantNameLookup = ConstantPoolLookupImpl(constantPool)
            val accessFlags = input.readShort()
            val thisClass = input.readShort()
            val superClass = input.readShort()
            val interfacesCount = input.readShort()
            val interfaces = List(interfacesCount.toInt()) { input.readShort() }
            val fieldsCount = input.readShort()
            val fields = List(fieldsCount.toInt()) { FieldInfo.fromDataInput(input, constantNameLookup) }
            val methodsCount = input.readShort()
            val methods = List(methodsCount.toInt()) { MethodInfo.fromDataInput(input, constantNameLookup) }
            val attributesCount = input.readShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantNameLookup) }
            return ClassFile(
                magic,
                minorVersion,
                majorVersion,
                constantPoolCount,
                constantPool,
                accessFlags,
                thisClass,
                superClass,
                interfacesCount,
                interfaces,
                fieldsCount,
                fields,
                methodsCount,
                methods,
                attributesCount,
                attributes
            )
        }

        fun readConstantPool(input: DataInput, count: Short): List<ConstantInfo> {
            var index = 1
            val constantPool = mutableListOf<ConstantInfo>()
            while (index < count) {
                val constantInfo = ConstantInfoFactory.fromDataInput(index, input)
                constantPool.add(constantInfo)
                index += constantInfo.entriesTaken
            }
            return constantPool
        }
    }
}
