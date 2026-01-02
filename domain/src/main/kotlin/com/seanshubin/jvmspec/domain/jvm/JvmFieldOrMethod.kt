package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

interface JvmFieldOrMethod {
    fun className(): String
    fun name(): String
    fun signature(): Signature
    fun attributes(): List<JvmAttribute>
    fun accessFlags(): Set<AccessFlag>
    fun ref(): JvmRef {
        return JvmRef(className(), name(), signature())
    }
}
