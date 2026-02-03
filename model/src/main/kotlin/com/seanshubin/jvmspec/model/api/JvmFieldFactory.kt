package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.FieldInfo

interface JvmFieldFactory {
    fun createField(jvmClass: JvmClass, fieldInfo: FieldInfo): JvmField
}
