package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

interface ApiClass {
    fun origin(): String
    fun magic(): Int
    fun minorVersion(): Int
    fun majorVersion(): Int
    fun thisClassName(): String
    fun superClassName(): String
    fun methods(): List<ApiMethod>
    fun disassemblyLines(): List<String>
    val constants: SortedMap<Int, ApiConstant.Constant>
    fun accessFlags(): Set<AccessFlag>
    fun interfaces(): List<ApiConstant.Constant>
    fun fields(): List<ApiField>
    fun attributes(): List<ApiAttribute>
}
