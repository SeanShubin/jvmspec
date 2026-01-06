package com.seanshubin.jvmspec.rules

sealed class RuleQuantifier {
    data class ZeroOrMore(val predicate: Predicate) : RuleQuantifier()
    data class Exactly(val count: Int, val predicate: Predicate) : RuleQuantifier()
    object AtEnd : RuleQuantifier()
}
