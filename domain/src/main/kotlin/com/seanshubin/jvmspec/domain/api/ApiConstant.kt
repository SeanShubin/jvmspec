package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

sealed interface ApiConstant {
    data class Constant(val index: Int, val tagId: Int, val tagName: String, val parts: List<ApiConstant>) : ApiConstant
    data class StringValue(val s: String) : ApiConstant
    data class IntegerValue(val i: Int) : ApiConstant
    data class FloatValue(val f: Float) : ApiConstant
    data class LongValue(val l: Long) : ApiConstant
    data class DoubleValue(val d: Double) : ApiConstant
    data class ReferenceKindValue(val id: Int, val name: String) : ApiConstant
    data class IndexValue(val index: Int) : ApiConstant
    companion object {
        fun utf8ToString(constant: Constant): String {
            require(constant.tagName == ConstantPoolTag.UTF8.name)
            val part = constant.parts[0] as StringValue
            return part.s
        }

        fun classToString(constant: Constant): String {
            require(constant.tagName == ConstantPoolTag.CLASS.name)
            val part = constant.parts[0] as Constant
            return utf8ToString(part)
        }

        fun nameAndTypeToStrings(constant: Constant): Pair<String, String> {
            require(constant.tagName == ConstantPoolTag.NAME_AND_TYPE.name)
            val part0 = constant.parts[0] as Constant
            val part1 = constant.parts[1] as Constant
            return Pair(
                utf8ToString(part0),
                utf8ToString(part1)
            )
        }

        fun refToStrings(constant: Constant): Triple<String, String, String> {
            require(
                constant.tagName == ConstantPoolTag.METHOD_REF.name ||
                        constant.tagName == ConstantPoolTag.FIELD_REF.name ||
                        constant.tagName == ConstantPoolTag.INTERFACE_METHOD_REF.name
            )
            val partClass = constant.parts[0] as Constant
            val partNameAndType = constant.parts[1] as Constant
            val className = classToString(partClass)
            val (methodName, methodDescriptor) = nameAndTypeToStrings(partNameAndType)
            return Triple(className, methodName, methodDescriptor)
        }
    }
}
