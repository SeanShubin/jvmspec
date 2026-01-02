package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.MethodInfo
import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.jvm.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

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
        return jvmClass.lookupSignature(methodDescriptorIndex)
    }

    override fun javaSignature(): String {
        val className = className()
        val methodName = name()
        val signature = signature()
        return signature.javaFormat(className, methodName)
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
