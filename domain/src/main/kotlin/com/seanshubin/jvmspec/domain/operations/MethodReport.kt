package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.aggregation.Aggregator
import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.WriteLines
import com.seanshubin.jvmspec.domain.util.StringListRuleMatcher
import java.nio.file.Path

class MethodReport(private val aggregator: Aggregator) : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: ApiClass): List<Command> {
        aggregator.classFile(classFile)
        val className = classFile.thisClassName()
        val methods = classFile.methods()
        val lines = mutableListOf<String>()
        val targetOpCodes = listOf(
            "invokestatic",
            "getstatic",
            "putstatic",
            "new"
        )
        methods.forEach { method ->
            val methodName = method.name()
            val methodSignature = method.signature()
            val javaFormat = methodSignature.javaFormat(className, methodName)
            val code = method.code()
            if (code == null) {
                lines.add(javaFormat)
            } else {
                val cyclomaticComplexity = code.complexity()
                aggregator.cyclomaticComplexity(method, cyclomaticComplexity)
                val categories = categoriesFor(method, code.opcodes())
                lines.add("categories=$categories complexity=$cyclomaticComplexity $javaFormat")
                aggregator.methodCategories(method, categories)
                code.instructions().forEach { instruction ->
                    val opCode = instruction.opcode()
                    if (targetOpCodes.contains(opCode)) {
                        val line = instruction.line()
                        lines.add("  $line")
                    }
                    if (opCode == "invokestatic") {
                        val target = instruction.arg1Ref()
                        aggregator.invokeStatic(method, target)
                    }
                    if (opCode == "getstatic") {
                        val target = instruction.arg1Ref()
                        aggregator.getStatic(method, target)
                    }
                    if (opCode == "putstatic") {
                        val target = instruction.arg1Ref()
                        aggregator.putStatic(method, target)
                    }
                    if (opCode == "new") {
                        val target = instruction.arg1ClassName()
                        aggregator.newInstance(method, target)
                    }
                }
            }
        }
        val outputFile = outputDir.resolve("$baseFileName-methods.txt")
        val commands = listOf(
            WriteLines(outputFile, lines)
        )
        return commands
    }

    companion object {
        fun categoriesFor(method: ApiMethod, opcodes: List<String>): Set<String> {
            return categoryMap.flatMap { (predicate, category) ->
                if (predicate(method, opcodes)) setOf(category) else emptySet()
            }.toSet()
        }

        val categoryMap = mapOf(
            ::isStaticPassThrough to "static-pass-through",
            ::isDelegatePassThrough to "delegate-pass-through",
            ::isDefaultConstructor to "default-constructor",
            ::isDefaultConstructorDelegate to "default-constructor-delegate",
            ::isSingletonGetter to "singleton-getter",
            ::isSingletonGetterDelegate to "singleton-getter-delegate",
            ::isCatchRethrow to "catch-rethrow",
            ::isCatchRethrowPassThrough to "catch-rethrow-pass-through",
            ::isSingletonStaticInitializer to "singleton-static-initializer"
        )

        fun isStaticPassThrough(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokestatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isDelegatePassThrough(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "getfield" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokeinterface" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isCatchRethrow(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "getfield" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokeinterface" }
            ruleMatcher.expectExactly(1) { it.contains("return") || it == "goto" }
            ruleMatcher.expectExactly(1) { it.contains("store") }
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokevirtual" }
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it.contains("throw") }
            ruleMatcher.expectZeroOrMore { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isCatchRethrowPassThrough(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokestatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") || it == "goto" }
            ruleMatcher.expectZeroOrMore { it.contains("store") }
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokevirtual" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it.contains("throw") }
            ruleMatcher.expectZeroOrMore { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isDefaultConstructor(method: ApiMethod, opcodes: List<String>): Boolean {
            if (method.name() != "<init>") return false
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isDefaultConstructorDelegate(method: ApiMethod, opcodes: List<String>): Boolean {
            if (method.name() != "<init>") return false
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "putfield" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isSingletonGetter(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it == "getstatic" }
            ruleMatcher.expectExactly(1) { it == "ifnonnull" }
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it == "putstatic" }
            ruleMatcher.expectExactly(1) { it == "getstatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isSingletonStaticInitializer(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it == "putstatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isSingletonGetterDelegate(method: ApiMethod, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it == "getstatic" }
            ruleMatcher.expectExactly(1) { it == "ifnonnull" }
            ruleMatcher.expectExactly(1) { it == "invokestatic" }
            ruleMatcher.expectZeroOrMore { it.contains("store") }
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it == "putstatic" }
            ruleMatcher.expectExactly(1) { it == "getstatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }
    }
}
