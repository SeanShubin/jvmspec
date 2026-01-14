package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.descriptor.Signature
import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag

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
