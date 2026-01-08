package com.seanshubin.jvmspec.rules

import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryTest {
    companion object {
        private fun createCategory(): Category {
            val objectMapper = RuleMapperFactory.createObjectMapper()
            val ruleSet = objectMapper.readValue(SampleData.rulesJson, CategoryRuleSet::class.java)
            return Category(ruleSet.categories)
        }
    }

    @Test
    fun staticPassThrough() {
        val methodName = "equals"
        val opcodeNames = listOf(
            "aload_0",
            "aload_1",
            "invokestatic",
            "ireturn"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("static-pass-through")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun delegatePassThrough() {
        val methodName = "executeStream"
        val opcodeNames = listOf(
            "aload_0",
            "getfield",
            "aload_1",
            "aload_2",
            "invokeinterface",
            "areturn"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("delegate-pass-through")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun defaultConstructor() {
        val methodName = "<init>"
        val opcodeNames = listOf(
            "aload_0",
            "invokespecial",
            "return"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("default-constructor")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun defaultConstructorDelegate() {
        val methodName = "<init>"
        val opcodeNames = listOf(
            "aload_0",
            "invokespecial",
            "aload_0",
            "iload_1",
            "putfield",
            "return"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("default-constructor-delegate")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun singletonGetter() {
        val methodName = "getInstance"
        val opcodeNames = listOf(
            "getstatic",
            "ifnonnull",
            "new",
            "dup",
            "invokespecial",
            "putstatic",
            "getstatic",
            "areturn"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("singleton-getter")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun catchRethrowPassThrough() {
        val methodName = "createConnectionConfig"
        val opcodeNames = listOf(
            "aload_1",
            "invokestatic",
            "areturn",
            "astore_2",
            "new",
            "dup",
            "aload_2",
            "invokevirtual",
            "aload_2",
            "invokespecial",
            "athrow"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("catch-rethrow-pass-through")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun singletonStaticInitializer() {
        val methodName = "<clinit>"
        val opcodeNames = listOf(
            "new",
            "dup",
            "invokespecial",
            "putstatic",
            "return"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("singleton-static-initializer")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun singletonGetterDelegate() {
        val methodName = "defaultInstance"
        val opcodeNames = listOf(
            "getstatic",
            "ifnonnull",
            "invokestatic",
            "astore_0",
            "new",
            "dup",
            "aload_0",
            "invokespecial",
            "putstatic",
            "getstatic",
            "areturn"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("singleton-getter-delegate")
        assertEquals(expectedCategories, actualCategories)
    }

    @Test
    fun catchRethrow() {
        val methodName = "newInputStream"
        val opcodeNames = listOf(
            "aload_0",
            "getfield",
            "aload_1",
            "aload_2",
            "invokeinterface",
            "areturn",
            "astore_3",
            "new",
            "dup",
            "aload_3",
            "invokevirtual",
            "aload_3",
            "invokespecial",
            "athrow"
        )
        val actualCategories = createCategory().fromMethodNameAndOpcodes(methodName, opcodeNames)
        val expectedCategories = setOf("catch-rethrow")
        assertEquals(expectedCategories, actualCategories)
    }
}
