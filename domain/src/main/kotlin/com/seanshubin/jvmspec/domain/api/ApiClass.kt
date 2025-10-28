package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

interface ApiClass {
    val origin: String
    val magic: Int
    val minorVersion: Int
    val majorVersion: Int
    val thisClassName: String
    val superClassName: String
    fun methods(): List<ApiMethod>
    fun disassemblyLines(): List<String>
    val constants: SortedMap<Int, ApiConstant.Constant>
    val accessFlags: Set<AccessFlag>
    fun interfaces(): List<ApiConstant.Constant>
    fun fields(): List<ApiField>
    fun attributes(): List<ApiAttribute>
}
