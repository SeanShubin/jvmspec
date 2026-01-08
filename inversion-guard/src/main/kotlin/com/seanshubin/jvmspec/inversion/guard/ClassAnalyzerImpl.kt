package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.jvm.*
import com.seanshubin.jvmspec.domain.tree.Tree
import com.seanshubin.jvmspec.rules.RuleInterpreter
import java.nio.file.Path

class ClassAnalyzerImpl(
    private val coreBoundaryMatcher: RegexMatcher,
    private val ruleInterpreter: RuleInterpreter
) : ClassAnalyzer {
    override fun analyzeClass(jvmClass: JvmClass): ClassAnalysis {
        val path = jvmClass.origin
        val name = jvmClass.thisClassName
        val methodAnalysisList: List<MethodAnalysis> = analyzeMethods(jvmClass)
        return ClassAnalysis(path, name, methodAnalysisList)
    }

    private fun analyzeMethods(jvmClass: JvmClass): List<MethodAnalysis> {
        return jvmClass.methods().map(::analyzeMethod)
    }

    private fun analyzeMethod(method: JvmMethod): MethodAnalysis {
        val className = method.className()
        val methodName = method.name()
        val signature = method.signature()
        val complexity = method.complexity()
        val boundaryLogicCategories = boundaryLogicCategories(method)
        if (boundaryLogicCategories.isNotEmpty()) {
            println("Boundary logic in $className.$methodName")
        }
        val staticInvocations = staticInvocations(method)
        return MethodAnalysis(
            className,
            methodName,
            signature,
            complexity,
            boundaryLogicCategories,
            staticInvocations
        )
    }

    private fun boundaryLogicCategories(method: JvmMethod): Set<String> {
        val name = method.name()
        val opcodes = method.instructions().map { it.name() }
        val categories = ruleInterpreter.evaluateCategories(name, opcodes)
        return categories
    }

    private fun staticInvocations(method: JvmMethod): List<InvocationAnalysis> {
        val invocationAnalysisList = mutableListOf<InvocationAnalysis>()
        val relevantOpcodeNames = setOf("invokestatic", "getstatic", "putstatic")
        method.instructions().forEach { instruction ->
            val opcodeName = instruction.name()
            if (relevantOpcodeNames.contains(opcodeName)) {
                val args = instruction.args()
                val firstArg = args[0] as JvmArgument.Constant
                val constant = firstArg.value as JvmConstant.JvmConstantRef
                val className = constant.className
                val methodName = constant.jvmNameAndType.name
                val methodDescriptor = constant.jvmNameAndType.descriptor
                val signature = DescriptorParser.build(methodDescriptor)
                val invocationType = checkInvocationType(className, methodName, signature)
                invocationAnalysisList.add(
                    InvocationAnalysis(
                        className,
                        methodName,
                        signature,
                        opcodeName,
                        invocationType
                    )
                )
            }
        }
        return invocationAnalysisList
    }

    private fun checkInvocationType(className: String, methodName: String, signature: Signature): InvocationType {
        val compact = signature.compactFormat(className, methodName)
        val invocationType = when (coreBoundaryMatcher.match(compact)) {
            RegexMatcher.MatchResult.NEITHER -> InvocationType.UNKNOWN
            RegexMatcher.MatchResult.INCLUDE_ONLY -> InvocationType.CORE
            else -> InvocationType.BOUNDARY
        }
        return invocationType
    }

    private fun createAnalysisCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val outputFile = Path.of("$baseFileName-analysis.txt")
        val complexity = jvmClass.complexity()
        val summaryRoots = listOf(
            Tree("Class: ${jvmClass.thisClassName}"),
            Tree("Origin: ${jvmClass.origin}"),
            Tree("Complexity: $complexity")
        )
        val methodChildren: List<Tree> = jvmClass.methods().mapIndexed { index, method ->
            val instructionMap = method.instructions().groupBy { it.name() }
            val invokeStaticNodes = createInstructionNode(instructionMap, "invokestatic")
            val putStaticNodes = createInstructionNode(instructionMap, "getstatic")
            val getStaticNodes = createInstructionNode(instructionMap, "putstatic")
            val staticNodes = invokeStaticNodes + putStaticNodes + getStaticNodes
            val methodHeader = "[$index]: complexity(${method.complexity()}) ${method.javaSignature()}"
            val methodRoot = Tree(methodHeader, staticNodes)
            methodRoot
        }
        val methodsHeader = "Methods(${jvmClass.methods().size}):"
        val methodsRoot: Tree = Tree(methodsHeader, methodChildren)
        val methodRoots = listOf(methodsRoot)
        val roots: List<Tree> = summaryRoots + methodRoots
        val command = CreateFileCommand(outputFile, roots)
        return listOf(command)
    }

    private fun createInstructionNode(instructionMap: Map<String, List<JvmInstruction>>, name: String): List<Tree> {
        val relevant = instructionMap[name] ?: return emptyList()
        val root = "$name (${relevant.size})"
        val children = relevant.map {
            val args = it.args()
            val firstArg = args[0] as JvmArgument.Constant
            val constant = firstArg.value as JvmConstant.JvmConstantRef
            val className = constant.className
            val methodName = constant.jvmNameAndType.name
            val methodDescriptor = constant.jvmNameAndType.descriptor
            val signature = DescriptorParser.build(methodDescriptor)
            val javaSignature = signature.javaFormat(className, methodName)
            Tree(javaSignature)
        }
        return listOf(Tree(root, children))
    }
}