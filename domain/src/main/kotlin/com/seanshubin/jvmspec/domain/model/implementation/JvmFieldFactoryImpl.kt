package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.FieldInfo
import com.seanshubin.jvmspec.domain.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmField
import com.seanshubin.jvmspec.domain.model.api.JvmFieldFactory

class JvmFieldFactoryImpl(
    private val attributeFactory: JvmAttributeFactory
) : JvmFieldFactory {
    override fun createField(jvmClass: JvmClass, fieldInfo: FieldInfo): JvmField =
        JvmFieldImpl(jvmClass, fieldInfo, attributeFactory)
}
