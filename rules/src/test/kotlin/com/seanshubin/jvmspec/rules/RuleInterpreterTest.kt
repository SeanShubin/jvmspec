package com.seanshubin.jvmspec.rules

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuleInterpreterTest {

    // Unit tests for Predicate operations

    @Test
    fun `predicate contains should match substring`() {
        val predicate = Predicate.Contains("load")
        assertTrue(predicate.evaluate("aload"))
        assertTrue(predicate.evaluate("iload"))
        assertTrue(predicate.evaluate("load_0"))
        assertFalse(predicate.evaluate("store"))
    }

    @Test
    fun `predicate equals should match exact string`() {
        val predicate = Predicate.Equals("invokestatic")
        assertTrue(predicate.evaluate("invokestatic"))
        assertFalse(predicate.evaluate("invokespecial"))
        assertFalse(predicate.evaluate("invoke"))
    }

    @Test
    fun `predicate or should match any of the predicates`() {
        val predicate = Predicate.Or(
            listOf(
                Predicate.Contains("return"),
                Predicate.Equals("goto")
            )
        )
        assertTrue(predicate.evaluate("return"))
        assertTrue(predicate.evaluate("ireturn"))
        assertTrue(predicate.evaluate("areturn"))
        assertTrue(predicate.evaluate("goto"))
        assertFalse(predicate.evaluate("invokestatic"))
    }

    // Integration tests for specific categories

    @Test
    fun `static-pass-through should match simple static delegation`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "static-pass-through" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokestatic")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        // Should match: no loads, invokestatic, return
        val opcodes1 = listOf("invokestatic", "return")
        assertEquals(setOf("static-pass-through"), interpreter.evaluateCategories("testMethod", opcodes1))

        // Should match: with loads
        val opcodes2 = listOf("aload", "iload", "invokestatic", "areturn")
        assertEquals(setOf("static-pass-through"), interpreter.evaluateCategories("testMethod", opcodes2))

        // Should not match: missing invokestatic
        val opcodes3 = listOf("aload", "return")
        assertEquals(emptySet(), interpreter.evaluateCategories("testMethod", opcodes3))

        // Should not match: extra instructions after return
        val opcodes4 = listOf("invokestatic", "return", "nop")
        assertEquals(emptySet(), interpreter.evaluateCategories("testMethod", opcodes4))
    }

    @Test
    fun `delegate-pass-through should match instance delegation pattern`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "delegate-pass-through" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("getfield")),
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokeinterface")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        // Should match: aload, getfield, aload, invokeinterface, return
        val opcodes = listOf("aload", "getfield", "aload", "invokeinterface", "areturn")
        assertEquals(setOf("delegate-pass-through"), interpreter.evaluateCategories("testMethod", opcodes))

        // Should not match: missing getfield
        val opcodesNoGetfield = listOf("aload", "aload", "invokeinterface", "areturn")
        assertEquals(emptySet(), interpreter.evaluateCategories("testMethod", opcodesNoGetfield))
    }

    @Test
    fun `default-constructor should require method name constraint`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "default-constructor" to CategoryRule(
                    methodConstraints = MethodConstraints(
                        methodName = Predicate.Equals("<init>")
                    ),
                    rules = listOf(
                        RuleQuantifier.Exactly(1, Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokespecial")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        val opcodes = listOf("aload", "invokespecial", "return")

        // Should match with <init> method name
        assertEquals(setOf("default-constructor"), interpreter.evaluateCategories("<init>", opcodes))

        // Should not match with regular method name
        assertEquals(emptySet(), interpreter.evaluateCategories("regularMethod", opcodes))
    }

    @Test
    fun `catch-rethrow should handle or predicate`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "catch-rethrow" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.Exactly(1, Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("getfield")),
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokeinterface")),
                        RuleQuantifier.Exactly(
                            1, Predicate.Or(
                                listOf(
                                    Predicate.Contains("return"),
                                    Predicate.Equals("goto")
                                )
                            )
                        ),
                        RuleQuantifier.Exactly(1, Predicate.Contains("store")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("new")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("dup")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokevirtual")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokespecial")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("throw")),
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        // Should match with return
        val opcodesWithReturn = listOf(
            "aload", "getfield", "aload", "invokeinterface", "areturn",
            "astore", "new", "dup", "aload", "invokevirtual", "aload",
            "invokespecial", "athrow"
        )
        assertEquals(setOf("catch-rethrow"), interpreter.evaluateCategories("testMethod", opcodesWithReturn))

        // Should match with goto
        val opcodesWithGoto = listOf(
            "aload", "getfield", "aload", "invokeinterface", "goto",
            "astore", "new", "dup", "aload", "invokevirtual", "aload",
            "invokespecial", "athrow"
        )
        assertEquals(setOf("catch-rethrow"), interpreter.evaluateCategories("testMethod", opcodesWithGoto))
    }

    @Test
    fun `singleton-getter should match lazy initialization pattern`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "singleton-getter" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.Exactly(1, Predicate.Equals("getstatic")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("ifnonnull")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("new")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("dup")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("invokespecial")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("putstatic")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("getstatic")),
                        RuleQuantifier.Exactly(1, Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        val opcodes = listOf(
            "getstatic", "ifnonnull", "new", "dup", "invokespecial",
            "putstatic", "getstatic", "areturn"
        )
        assertEquals(setOf("singleton-getter"), interpreter.evaluateCategories("getInstance", opcodes))
    }

    @Test
    fun `test multiple categories can match`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "simple" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.Exactly(1, Predicate.Equals("return")),
                        RuleQuantifier.AtEnd
                    )
                ),
                "also-simple" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.Exactly(1, Predicate.Contains("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        val opcodes = listOf("return")
        val result = interpreter.evaluateCategories("testMethod", opcodes)
        // Both rules should match
        assertEquals(2, result.size)
        assertTrue(result.contains("simple"))
        assertTrue(result.contains("also-simple"))
    }

    @Test
    fun `test zero-or-more quantifier`() {
        val ruleSet = CategoryRuleSet(
            mapOf(
                "flexible" to CategoryRule(
                    methodConstraints = null,
                    rules = listOf(
                        RuleQuantifier.ZeroOrMore(Predicate.Contains("load")),
                        RuleQuantifier.Exactly(1, Predicate.Equals("return")),
                        RuleQuantifier.AtEnd
                    )
                )
            )
        )
        val interpreter = RuleInterpreter(ruleSet.categories)

        // Match with zero loads
        val opcodes0 = listOf("return")
        assertEquals(setOf("flexible"), interpreter.evaluateCategories("testMethod", opcodes0))

        // Match with one load
        val opcodes1 = listOf("aload", "return")
        assertEquals(setOf("flexible"), interpreter.evaluateCategories("testMethod", opcodes1))

        // Match with multiple loads
        val opcodesN = listOf("aload", "iload", "aload", "return")
        assertEquals(setOf("flexible"), interpreter.evaluateCategories("testMethod", opcodesN))
    }
}
