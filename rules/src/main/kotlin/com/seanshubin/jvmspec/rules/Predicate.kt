package com.seanshubin.jvmspec.rules

sealed class Predicate {
    abstract fun evaluate(value: String): Boolean

    data class Contains(val value: String) : Predicate() {
        override fun evaluate(value: String): Boolean = value.contains(this.value)
    }

    data class Equals(val value: String) : Predicate() {
        override fun evaluate(value: String): Boolean = value == this.value
    }

    data class Or(val predicates: List<Predicate>) : Predicate() {
        override fun evaluate(value: String): Boolean =
            predicates.any { it.evaluate(value) }
    }
}
