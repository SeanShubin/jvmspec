package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.AccessFlag

interface ApiClass {
    fun origin(): String
    fun magic(): Int
    fun minorVersion(): Int
    fun majorVersion(): Int
    fun thisClassName(): String
    fun superClassName(): String
    fun methods(): List<ApiMethod>
    fun disassemblyLines(): List<String>
    fun constants(): List<ApiConstant.Constant>
    fun accessFlags(): Set<AccessFlag>
    fun interfaces(): List<ApiConstant.Constant>
}
