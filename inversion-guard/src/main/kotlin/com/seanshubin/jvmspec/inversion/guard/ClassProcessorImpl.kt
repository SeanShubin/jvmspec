package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.descriptor.DescriptorParser
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.jvm.JvmArgument
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmInstruction
import com.seanshubin.jvmspec.domain.prototype.JvmConstant
import com.seanshubin.jvmspec.domain.prototype.asStrings
import com.seanshubin.jvmspec.domain.tree.Tree
import com.seanshubin.jvmspec.domain.util.PathUtil.removeExtension
import com.seanshubin.jvmspec.rules.CategoryRule
import java.nio.file.Path

class ClassProcessorImpl(
    private val baseDir: Path,
    private val outputDir: Path,
    private val format: JvmSpecFormat,
    private val core: List<String>,
    private val boundary: List<String>,
    private val categoryRuleSet: Map<String, CategoryRule>

) : ClassProcessor {
    override fun processClass(jvmClass: JvmClass): List<Command> {
        val relativePath = baseDir.relativize(jvmClass.origin)
        val baseFileName = outputDir.resolve(relativePath).removeExtension("class")
        val analysisCommands = createAnalysisCommands(baseFileName, jvmClass)
        val disassemblyCommands = createDisassemblyCommands(baseFileName, jvmClass)
        val allCommands = analysisCommands + disassemblyCommands
        return allCommands
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
            val (className, methodName, methodDescriptor) = constant.asStrings()
            val signature = DescriptorParser.build(methodDescriptor)
            val javaSignature = signature.javaFormat(className, methodName)
            Tree(javaSignature)
        }
        return listOf(Tree(root, children))
    }

    private fun createDisassemblyCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val treeList = format.classTreeList(jvmClass)
        val filePath = Path.of("$baseFileName-disassembled.txt")
        val disassemblyCommand = CreateFileCommand(filePath, treeList)
        return listOf(disassemblyCommand)
    }
}