package com.seanshubin.jvmspec.domain.aggregation

import kotlin.test.Test
import kotlin.test.assertEquals

class QualifiedMethodTest {
    @Test
    fun typical() {
        val input = QualifiedMethod(
            className = "java/lang/System",
            methodName = "getProperty",
            methodDescriptor = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
        )
        val expected = "String System.getProperty(String, String)"

        val actual = input.javaSignature()
        assertEquals(expected, actual)
    }
}
