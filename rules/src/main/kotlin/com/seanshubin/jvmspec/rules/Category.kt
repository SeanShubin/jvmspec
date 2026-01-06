package com.seanshubin.jvmspec.rules

class Category(private val ruleSet: CategoryRuleSet) {
    private val interpreter: RuleInterpreter = RuleInterpreter(ruleSet)

    fun fromMethodNameAndOpcodes(methodName: String, opcodeNames: List<String>): Set<String> {
        return interpreter.evaluateCategories(methodName, opcodeNames)
    }
}