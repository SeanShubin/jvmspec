package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.descriptor.Signature
import com.seanshubin.jvmspec.domain.jvm.JvmArgument
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmConstant
import com.seanshubin.jvmspec.domain.jvm.JvmMethod
import com.seanshubin.jvmspec.rules.RuleInterpreter

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
}