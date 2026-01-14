package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.FieldInfo

interface JvmFieldFactory {
    fun createField(jvmClass: JvmClass, fieldInfo: FieldInfo): JvmField
}
