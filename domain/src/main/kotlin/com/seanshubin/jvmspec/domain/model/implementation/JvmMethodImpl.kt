package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.descriptor.Signature
import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.domain.classfile.structure.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.classfile.structure.MethodInfo
import com.seanshubin.jvmspec.domain.model.api.*

class JvmMethodImpl(
    private val jvmClass: JvmClass,
    private val methodInfo: MethodInfo
) : JvmMethod {
    override fun accessFlags(): Set<AccessFlag> {
        return methodInfo.accessFlags
    }

    override fun className(): String {
        return jvmClass.thisClassName
    }

    override fun name(): String {
        val methodNameIndex = methodInfo.nameIndex
        return jvmClass.lookupUtf8(methodNameIndex)
    }

    override fun signature(): Signature {
        val methodDescriptorIndex = methodInfo.descriptorIndex
        val descriptor = jvmClass.lookupDescriptor(methodDescriptorIndex)
        return Signature(className(), name(), descriptor)
    }

    override fun attributes(): List<JvmAttribute> {
        return methodInfo.attributes.map { attribute ->
            when (attribute) {
                is AttributeCodeInfo -> JvmCodeAttributeImpl(jvmClass, attribute)
                else -> JvmAttributeImpl(jvmClass, attribute)
            }
        }
    }

    override fun code(): JvmCodeAttribute? {
        val codeAttributes = attributes().filterIsInstance<JvmCodeAttribute>()
        return if (codeAttributes.isEmpty()) {
            null
        } else if (codeAttributes.size == 1) {
            codeAttributes[0]
        } else {
            throw RuntimeException("Zero or one Code attributes expected, got ${codeAttributes.size}")
        }
    }

    override fun instructions(): List<JvmInstruction> {
        return code()?.instructions() ?: emptyList()
    }

    override fun complexity(): Int {
        val code = code() ?: return 0
        return code.complexity()
    }
}
