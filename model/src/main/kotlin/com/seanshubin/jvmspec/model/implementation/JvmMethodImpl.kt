package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.descriptor.Signature
import com.seanshubin.jvmspec.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.classfile.structure.MethodInfo
import com.seanshubin.jvmspec.model.api.*

class JvmMethodImpl(
    private val jvmClass: JvmClass,
    private val methodInfo: MethodInfo,
    private val attributeFactory: JvmAttributeFactory
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
        val toJvmAttribute = { attribute: com.seanshubin.jvmspec.classfile.structure.AttributeInfo ->
            attributeFactory.createAttribute(jvmClass, attribute)
        }
        return methodInfo.attributes.map(toJvmAttribute)
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
