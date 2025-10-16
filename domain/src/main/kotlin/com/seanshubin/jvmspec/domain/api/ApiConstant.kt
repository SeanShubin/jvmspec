package com.seanshubin.jvmspec.domain.api

sealed interface ApiConstant {
    data class Constant(val index: Int, val tagId: Int, val tagName: String, val parts: List<ApiConstant>) : ApiConstant
    data class StringValue(val s: String) : ApiConstant
    data class IntegerValue(val i: Int) : ApiConstant
    data class FloatValue(val f: Float) : ApiConstant
    data class LongValue(val l: Long) : ApiConstant
    data class DoubleValue(val d: Double) : ApiConstant
    data class ReferenceKindValue(val id: Int, val name: String) : ApiConstant
    data class IndexValue(val index: Int) : ApiConstant
}
