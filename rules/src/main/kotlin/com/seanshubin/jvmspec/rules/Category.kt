package com.seanshubin.jvmspec.rules

class Category(private val categories: Map<String, CategoryRule>) {
    private val interpreter: RuleInterpreter = RuleInterpreter(categories)

    fun fromMethodNameAndOpcodes(methodName: String, opcodeNames: List<String>): Set<String> {
        return interpreter.evaluateCategories(methodName, opcodeNames)
    }
}