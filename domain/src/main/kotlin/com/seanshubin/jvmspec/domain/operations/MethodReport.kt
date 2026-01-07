package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.aggregation.Aggregator
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.WriteLines
import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.jvm.*
import com.seanshubin.jvmspec.domain.util.StringListRuleMatcher
import java.nio.file.Path

class MethodReport(
    private val aggregator: Aggregator,
    private val format: JvmSpecFormat,
    private val indent: (String) -> String
) : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: JvmClass): List<Command> {
        aggregator.classFile(classFile)
        val className = classFile.thisClassName
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
                val categories = categoriesFor(method.name(), code.instructions().map { it.name().lowercase() })
                lines.add("categories=$categories complexity=$cyclomaticComplexity $javaFormat")
                aggregator.methodCategories(method, categories)
                code.instructions().forEach { instruction ->
                    val name = instruction.name()
                    if (targetOpCodes.contains(name)) {
                        val instructionLines = format.instructionTree(instruction).toLines(indent)
                        lines.addAll(instructionLines)
                    }
                    if (name == "invokestatic") {
                        val target = getRef(instruction)
                        aggregator.invokeStatic(method, target)
                    }
                    if (name == "getstatic") {
                        val target = getRef(instruction)
                        aggregator.getStatic(method, target)
                    }
                    if (name == "putstatic") {
                        val target = getRef(instruction)
                        aggregator.putStatic(method, target)
                    }
                    if (name == "new") {
                        val target = getClassName(instruction)
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
        fun getRef(instruction: JvmInstruction): JvmRef {
            if (instruction.args().size != 1) {
                throw RuntimeException("Expected exactly one argument for instruction ${instruction.name()}")
            }
            val arg = instruction.args()[0]
            arg as JvmArgument.Constant
            val constant = arg.value as JvmConstant.JvmConstantRef
            val (className, methodName, methodDescriptor) = constant.asStrings()
            val signature = DescriptorParser.build(methodDescriptor)
            return JvmRef(className, methodName, signature)
        }

        fun getClassName(instruction: JvmInstruction): String {
            if (instruction.args().size != 1) {
                throw RuntimeException("Expected exactly one argument for instruction ${instruction.name()}")
            }
            val arg = instruction.args()[0]
            arg as JvmArgument.Constant
            val constant = arg.value as JvmConstant.JvmConstantClass
            val className = constant.asString()
            return className
        }

        fun categoriesFor(methodName: String, opcodes: List<String>): Set<String> {
            val categories = categoryMap.flatMap { (predicate, category) ->
                if (predicate(methodName, opcodes)) setOf(category) else emptySet()
            }.toSet()
            val target = "catch-rethrow"
            if (categories.contains(target)) {
                println("==========================================")
                println("target = $target")
                println("methodName = $methodName")
                println("opcodes(${opcodes.size})")
                opcodes.map { "  $it" }.forEach { println(it) }
                println("==========================================")
                println("==========================================")
            }
            return categories
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

        fun isStaticPassThrough(methodName: String, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokestatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isDelegatePassThrough(methodName: String, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "getfield" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokeinterface" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isCatchRethrow(methodName: String, opcodes: List<String>): Boolean {
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

        fun isCatchRethrowPassThrough(methodName: String, opcodes: List<String>): Boolean {
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

        fun isDefaultConstructor(methodName: String, opcodes: List<String>): Boolean {
            if (methodName != "<init>") return false
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isDefaultConstructorDelegate(methodName: String, opcodes: List<String>): Boolean {
            if (methodName != "<init>") return false
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectZeroOrMore { it.contains("load") }
            ruleMatcher.expectExactly(1) { it == "putfield" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isSingletonGetter(methodName: String, opcodes: List<String>): Boolean {
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

        fun isSingletonStaticInitializer(methodName: String, opcodes: List<String>): Boolean {
            val ruleMatcher = StringListRuleMatcher(opcodes)
            ruleMatcher.expectExactly(1) { it == "new" }
            ruleMatcher.expectExactly(1) { it == "dup" }
            ruleMatcher.expectExactly(1) { it == "invokespecial" }
            ruleMatcher.expectExactly(1) { it == "putstatic" }
            ruleMatcher.expectExactly(1) { it.contains("return") }
            ruleMatcher.expectAtEnd()
            return ruleMatcher.rulesMatched
        }

        fun isSingletonGetterDelegate(methodName: String, opcodes: List<String>): Boolean {
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
