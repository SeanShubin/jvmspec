package com.seanshubin.jvmspec.rules

class RuleInterpreter(private val categories: Map<String, CategoryRule>) {
    fun evaluateCategories(method: String, opcodes: List<String>): Set<String> {
        return categories.flatMap { (categoryName, categoryRule) ->
            if (evaluateCategory(method, opcodes, categoryRule)) {
                setOf(categoryName)
            } else {
                emptySet()
            }
        }.toSet()
    }

    private fun evaluateCategory(
        method: String,
        opcodes: List<String>,
        rule: CategoryRule
    ): Boolean {
        // Check method constraints first
        if (!evaluateMethodConstraints(method, rule.methodConstraints)) {
            return false
        }

        // Evaluate opcode sequence with inlined pattern matching
        var index = 0
        var rulesMatched = true

        for (quantifier in rule.rules) {
            if (!rulesMatched) break

            when (quantifier) {
                is RuleQuantifier.ZeroOrMore -> {
                    // expectZeroOrMore: consume while predicate matches
                    while (index < opcodes.size && quantifier.predicate.evaluate(opcodes[index])) {
                        index++
                    }
                }

                is RuleQuantifier.Exactly -> {
                    // expectExactly: consume exactly 'count' items that match predicate
                    if (quantifier.count != 0 && index >= opcodes.size) {
                        rulesMatched = false
                    }
                    val targetIndex = index + quantifier.count
                    while (rulesMatched && index < opcodes.size && index < targetIndex) {
                        if (!quantifier.predicate.evaluate(opcodes[index])) {
                            rulesMatched = false
                        }
                        index++
                    }
                }

                is RuleQuantifier.AtEnd -> {
                    // expectAtEnd: verify we've consumed all opcodes
                    if (index != opcodes.size) {
                        rulesMatched = false
                    }
                }
            }
        }
        return rulesMatched
    }

    private fun evaluateMethodConstraints(
        method: String,
        constraints: MethodConstraints?
    ): Boolean {
        if (constraints == null) return true

        constraints.methodName?.let { predicate ->
            if (!predicate.evaluate(method)) return false
        }

        return true
    }
}
