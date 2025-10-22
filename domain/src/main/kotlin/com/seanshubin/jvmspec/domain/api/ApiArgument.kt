package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.ArrayType

sealed interface ApiArgument {
    data class Constant(val value: ApiConstant) : ApiArgument
    data class IntValue(val value: Int) : ApiArgument
    data class ArrayTypeValue(val value: ArrayType) : ApiArgument
    data class LookupSwitch(val default: Int, val lookup: List<Pair<Int, Int>>) : ApiArgument
    data class TableSwitch(val default: Int, val low: Int, val high: Int, val jumpOffsets: List<Int>) : ApiArgument
    data class OpCodeValue(val name: String, val code: UByte) : ApiArgument
}
