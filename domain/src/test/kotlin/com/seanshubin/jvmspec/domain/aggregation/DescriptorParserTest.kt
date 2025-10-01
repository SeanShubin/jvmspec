package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.api.SignatureType
import com.seanshubin.jvmspec.domain.apiimpl.DescriptorParser
import kotlin.test.Test
import kotlin.test.assertEquals

class DescriptorParserTest {
    @Test
    fun javaExamples() {
        verify(
            "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
            "(java/lang/String,java/lang/String)java/lang/String"
        )
        verify("(Ljava/lang/String;I)V", "(java/lang/String,int)void")
        verify("()[Ljava/lang/String;", "()java/lang/String[1]")
        verify("([Ljava/lang/String;)V", "(java/lang/String[1])void")
        verify("Ljava/util/Locale;", "java/util/Locale")
    }

    private fun SignatureType.testString(): String =
        if (dimensions == 0) name
        else "$name[$dimensions]"

    private fun verify(descriptor: String, expected: String) {
        val signatureParts = DescriptorParser.build(descriptor)
        val parameters = signatureParts.parameters
        val parametersPart = if (parameters == null) {
            ""
        } else {
            signatureParts.parameters.joinToString(
                ",",
                "(",
                ")"
            ) { it.testString() }
        }
        val actual = parametersPart + signatureParts.returnType.testString()
        assertEquals(expected, actual)
    }
}
