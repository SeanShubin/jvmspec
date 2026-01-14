package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.specification.ArrayType

sealed interface JvmArgument {
    data class Constant(val value: JvmConstant) : JvmArgument
    data class IntValue(val value: Int) : JvmArgument
    data class ArrayTypeValue(val value: ArrayType) : JvmArgument
    data class LookupSwitch(val default: Int, val lookup: List<Pair<Int, Int>>) : JvmArgument
    data class TableSwitch(val default: Int, val low: Int, val high: Int, val jumpOffsets: List<Int>) : JvmArgument
    data class OpCodeValue(val name: String, val code: UByte) : JvmArgument
}
