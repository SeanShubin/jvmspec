package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.apiimpl.ApiClassImpl
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.data.OriginClassFile
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths

object PrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val fileName = args[0]
        val file = Paths.get(fileName)
        val classFile = Files.newInputStream(file).use { inputStream ->
            val input = DataInputStream(inputStream)
            val origin = OriginClassFile(file)
            ClassFile.fromDataInput(origin, input)
        }
        val api = ApiClassImpl(classFile)
        display(api)
    }

    fun display(apiClass: ApiClass) {
        val className = apiClass.className()
        apiClass.methods().forEach { method ->
            val methodName = method.name()
            val javaSignature = method.signature().javaFormatUnqualified(className, methodName)
            val code = method.code()
            if (code == null) {
                println("no code for $javaSignature")
            } else {
                val complexity = code.complexity()
                val opcodes = code.opcodes()
                val categories = categoriesFor(methodName, opcodes)
                val opcodeList = opcodes.joinToString(",", "(", ")")
                println(javaSignature)
                println("[complexity=$complexity, categories=$categories opcodes=$opcodeList]")
                println()
            }
        }
    }

    fun categoriesFor(name: String, opcodes: List<String>): Set<String> {
        return categoryMap.flatMap { (predicate, category) ->
            if (predicate(name, opcodes)) setOf(category) else emptySet()
        }.toSet()
    }

    val categoryMap = mapOf(
        ::isPassThrough to "pass-through",
        ::isDefaultConstructor to "default-constructor",
        ::isSingletonGetter to "singleton-getter"
    )

    fun isPassThrough(name: String, opcodes: List<String>): Boolean {
        val opCodesLoad = opcodes.takeWhile { it.contains("load") }
        val opCodesAfterLoad = opcodes.drop(opCodesLoad.size)
        val opCodesInvokeStatic = opCodesAfterLoad.takeWhile { it == "invokestatic" }
        val opCodesAfterInvokeStatic = opCodesAfterLoad.drop(opCodesInvokeStatic.size)
        val opCodesReturn = opCodesAfterInvokeStatic.takeWhile { it.contains("return") }
        val opCodesAfterReturn = opCodesAfterInvokeStatic.drop(opCodesReturn.size)
        if (opCodesInvokeStatic.size != 1) return false
        if (opCodesReturn.size != 1) return false
        if (opCodesAfterReturn.isNotEmpty()) return false
        return true
    }

    fun isDefaultConstructor(name: String, opcodes: List<String>): Boolean {
        if (name != "<init>") return false
        if (opcodes != listOf("aload_0", "invokespecial", "return")) return false
        return true
    }

    fun isSingletonGetter(name: String, opcodes: List<String>): Boolean {
        return opcodes == listOf(
            "getstatic",
            "ifnonnull",
            "new",
            "dup",
            "invokespecial",
            "putstatic",
            "getstatic",
            "areturn"
        )
    }
}
