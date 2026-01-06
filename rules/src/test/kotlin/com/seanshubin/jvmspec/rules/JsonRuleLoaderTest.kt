package com.seanshubin.jvmspec.rules

import org.junit.Test
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonRuleLoaderTest {

    @Test
    fun `load rules from json file`() {
        // When running from maven, working directory is the rules module, so we need to go up one level
        val rulesPath = Paths.get("../rules.json")
        val loader = JsonRuleLoader(rulesPath)
        val ruleSet = loader.load()

        // Verify all 9 categories are loaded
        assertEquals(9, ruleSet.categories.size)

        val expectedCategories = setOf(
            "static-pass-through",
            "delegate-pass-through",
            "catch-rethrow",
            "catch-rethrow-pass-through",
            "default-constructor",
            "default-constructor-delegate",
            "singleton-getter",
            "singleton-static-initializer",
            "singleton-getter-delegate"
        )

        assertTrue(ruleSet.categories.keys.containsAll(expectedCategories))

        // Verify a specific category structure (delegate-pass-through)
        val delegatePassThrough = ruleSet.categories["delegate-pass-through"]!!
        assertEquals(null, delegatePassThrough.methodConstraints)
        assertEquals(6, delegatePassThrough.rules.size)

        // Verify rules are properly parsed
        assertTrue(delegatePassThrough.rules[0] is RuleQuantifier.ZeroOrMore)
        assertTrue(delegatePassThrough.rules[1] is RuleQuantifier.Exactly)
        assertTrue(delegatePassThrough.rules[5] is RuleQuantifier.AtEnd)

        // Verify method constraints (default-constructor)
        val defaultConstructor = ruleSet.categories["default-constructor"]!!
        assertTrue(defaultConstructor.methodConstraints != null)
        assertTrue(defaultConstructor.methodConstraints!!.methodName is Predicate.Equals)
        val methodNamePredicate = defaultConstructor.methodConstraints!!.methodName as Predicate.Equals
        assertEquals("<init>", methodNamePredicate.value)

        // Verify OR predicate (catch-rethrow)
        val catchRethrow = ruleSet.categories["catch-rethrow"]!!
        val orQuantifier = catchRethrow.rules[4] as RuleQuantifier.Exactly
        assertTrue(orQuantifier.predicate is Predicate.Or)
        val orPredicate = orQuantifier.predicate as Predicate.Or
        assertEquals(2, orPredicate.predicates.size)
    }
}
