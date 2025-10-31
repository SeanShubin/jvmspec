package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.jvmimpl.DescriptorParser
import kotlin.test.Test
import kotlin.test.assertEquals

class SignatureTest {
    @Test
    fun typical() {
        val methodDescriptor = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
        val signature = DescriptorParser.build(methodDescriptor)
        val className = "java/lang/System"
        val methodName = "getProperty"
        val expected = "String System.getProperty(String, String)"
        val actual = signature.javaFormat(className, methodName)
        assertEquals(expected, actual)
    }
}
