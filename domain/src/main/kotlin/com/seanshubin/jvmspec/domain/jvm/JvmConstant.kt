package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

sealed interface JvmConstant {
    data class Constant(val index: UShort, val tag: ConstantPoolTag, val parts: List<JvmConstant>) :
        JvmConstant

    data class StringValue(val s: String) : JvmConstant
    data class IntegerValue(val i: Int) : JvmConstant
    data class FloatValue(val f: Float) : JvmConstant
    data class LongValue(val l: Long) : JvmConstant
    data class DoubleValue(val d: Double) : JvmConstant
    data class ReferenceKindValue(val id: UByte, val name: String) : JvmConstant
    data class IndexValue(val index: UShort) : JvmConstant
    companion object {
        fun utf8ToString(constant: Constant): String {
            require(constant.tag == ConstantPoolTag.UTF8)
            val part = constant.parts[0] as StringValue
            return part.s
        }

        fun classToString(constant: Constant): String {
            require(constant.tag == ConstantPoolTag.CLASS)
            val part = constant.parts[0] as Constant
            return utf8ToString(part)
        }

        fun nameAndTypeToStrings(constant: Constant): Pair<String, String> {
            require(constant.tag == ConstantPoolTag.NAME_AND_TYPE)
            val part0 = constant.parts[0] as Constant
            val part1 = constant.parts[1] as Constant
            return Pair(
                utf8ToString(part0),
                utf8ToString(part1)
            )
        }

        fun refToStrings(constant: Constant): Triple<String, String, String> {
            require(
                constant.tag == ConstantPoolTag.METHOD_REF ||
                        constant.tag == ConstantPoolTag.FIELD_REF ||
                        constant.tag == ConstantPoolTag.INTERFACE_METHOD_REF
            )
            val partClass = constant.parts[0] as Constant
            val partNameAndType = constant.parts[1] as Constant
            val className = classToString(partClass)
            val (methodName, methodDescriptor) = nameAndTypeToStrings(partNameAndType)
            return Triple(className, methodName, methodDescriptor)
        }
    }
}
