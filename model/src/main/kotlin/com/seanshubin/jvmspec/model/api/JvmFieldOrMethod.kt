package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.descriptor.Signature
import com.seanshubin.jvmspec.classfile.specification.AccessFlag

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
