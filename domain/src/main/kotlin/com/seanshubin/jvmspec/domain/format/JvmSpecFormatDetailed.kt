package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import com.seanshubin.jvmspec.domain.tree.Tree
import java.util.*

class JvmSpecFormatDetailed : JvmSpecFormat {
    override fun classTreeList(apiClass: ApiClass): List<Tree> {
        return listOf(
            Tree("origin: ${apiClass.origin}"),
            Tree("magic: ${hexUpper(apiClass.magic)}"),
            Tree("minor version: ${apiClass.minorVersion}"),
            Tree("major version: ${apiClass.majorVersion}"),
            accessFlagsTree(apiClass.accessFlags),
            Tree("this_class: ${apiClass.thisClassName}"),
            Tree("super_class: ${apiClass.superClassName}"),
            constantsTree(apiClass.constants),
            interfacesTree(apiClass.interfaces()),
            fieldsTree(apiClass.fields()),
            methodsTree(apiClass.methods()),
            attributesTree(apiClass.attributes())
        )
    }

    private fun methodsTree(methods: List<ApiMethod>): Tree {
        val children = methods.mapIndexed { index, method -> fieldOrMethodTree("method", index, method) }
        val parent = Tree("methods(${children.size})", children)
        return parent
    }

    private fun fieldsTree(methods: List<ApiField>): Tree {
        val children = methods.mapIndexed { index, field -> fieldOrMethodTree("field", index, field) }
        val parent = Tree("fields(${children.size})", children)
        return parent
    }

    private fun interfacesTree(interfaces: List<ApiConstant.Constant>): Tree {
        val children = interfaces.map { constant ->
            constantTree(constant)
        }
        val parent = Tree("interfaces(${children.size})", children)
        return parent
    }

    private fun accessFlagsTree(accessFlags: Set<AccessFlag>): Tree {
        val accessFlagString = formatAccessFlags(accessFlags)
        val accessFlagTree = Tree("access_flags: $accessFlagString")
        return accessFlagTree
    }

    private fun constantsTree(constants: SortedMap<UShort, ApiConstant.Constant>): Tree {
        val children = constants.map { (index, constant) ->
            constantTree(constant)
        }
        val parent = Tree("constants(${children.size})", children)
        return parent
    }

    private fun constantTree(constant: ApiConstant): Tree {
        return when (constant) {
            is ApiConstant.Constant -> {
                val parent = "[${constant.index}] ${constant.tagName}(${constant.tagId})"
                val children = constant.parts.map { part -> constantTree(part) }
                Tree(parent, children)
            }

            is ApiConstant.StringValue -> Tree(constant.s.toSanitizedString())
            is ApiConstant.IntegerValue -> Tree("${constant.i}")
            is ApiConstant.FloatValue -> Tree("${constant.f}")
            is ApiConstant.LongValue -> Tree("${constant.l}")
            is ApiConstant.DoubleValue -> Tree("${constant.d}")
            is ApiConstant.ReferenceKindValue -> Tree("${constant.name}(${constant.id})")
            is ApiConstant.IndexValue -> Tree("${constant.index}")
        }
    }

    private fun fieldOrMethodTree(caption: String, index: Int, fieldOrMethod: ApiFieldOrMethod): Tree {
        val formattedSignature = formatSignature(
            fieldOrMethod.className().replace("/", "."),
            fieldOrMethod.name(),
            fieldOrMethod.signature()
        )
        val children = listOf(
            Tree("access flags = ${formatAccessFlags(fieldOrMethod.accessFlags())}"),
            Tree("signature = $formattedSignature"),
            attributesTree(fieldOrMethod.attributes())
        )
        val parent = Tree("$caption[$index]", children)
        return parent
    }

    private fun attributesTree(attributes: List<ApiAttribute>): Tree {
        val children = attributes.mapIndexed { index, attribute -> attributeTree(index, attribute) }
        val parent = Tree("attributes(${children.size})", children)
        return parent
    }

    private fun attributeTree(index: Int, attribute: ApiAttribute): Tree {
        val bytesNode = listOf(
            Tree(attribute.bytes().toHexString()),
            Tree(attribute.bytes().toSanitizedString())
        )
        val children = listOf(
            Tree("name: ${attribute.name()}"),
            Tree("bytes(${attribute.bytes().size})", bytesNode),
        )
        val codeList = if (attribute is ApiCodeAttribute) {
            listOf(codeTree(attribute))
        } else {
            emptyList()
        }
        return Tree("attribute[$index]", children + codeList)
    }

    private fun codeTree(codeAttribute: ApiCodeAttribute): Tree {
        val instructionsChildren = codeAttribute.instructions().map { instruction ->
            instructionTree(instruction)
        }
        val attributesTree = attributesTree(codeAttribute.attributes())
        val instructionsTree = Tree("instructions(${instructionsChildren.size})", instructionsChildren)
        val exceptionTableTree = exceptionTableListTree(codeAttribute.exceptionTable())
        return Tree("code", listOf(instructionsTree, exceptionTableTree, attributesTree))
    }

    private fun exceptionTableListTree(exceptions: List<ApiExceptionTable>): Tree {
        val children = exceptions.map { exception ->
            exceptionTableTree(exception)
        }
        val parent = Tree("exceptions(${children.size})", children)
        return parent
    }

    private fun exceptionTableTree(exception: ApiExceptionTable): Tree {
        exception.let {
            val children = listOf(
                Tree("start_pc: ${it.startProgramCounter}"),
                Tree("end_pc: ${it.endProgramCounter}"),
                Tree("handler_pc: ${it.handlerProgramCounter}"),
                Tree("catch_type: ${it.catchType}")
            )
            return Tree("exception", children)
        }
    }

    override fun instructionTree(apiInstruction: ApiInstruction): Tree {
        val argsTreeList = argsTreeList(apiInstruction.args())
        val name = apiInstruction.name()
        val code = apiInstruction.code()
        val parent = Tree("$name(0x${code.toHexString().uppercase()})", argsTreeList)
        return parent
    }

    private fun argsTreeList(argList: List<ApiArgument>): List<Tree> {
        return argList.map(::argsTree)
    }

    private fun argsTree(arg: ApiArgument): Tree {
        return when (arg) {
            is ApiArgument.Constant -> {
                constantTree(arg.value)
            }

            is ApiArgument.IntValue -> {
                Tree(arg.value.toString())
            }

            is ApiArgument.ArrayTypeValue -> {
                Tree("${arg.value.name}(${arg.value.code})")
            }

            is ApiArgument.LookupSwitch -> {
                val default = Tree(arg.default.toString())
                val pairs = arg.lookup.map { (match, offset) ->
                    Tree("$match:$offset")
                }
                val children = listOf(default) + pairs
                Tree("switch", children)
            }

            is ApiArgument.TableSwitch -> {
                val defaultTree = Tree("default", listOf(Tree(arg.default.toString())))
                val lowTree = Tree("low", listOf(Tree(arg.low.toString())))
                val highTree = Tree("high", listOf(Tree(arg.high.toString())))
                val jumpOffsetChildren = arg.jumpOffsets.map { Tree(it.toString()) }
                val jumpOffsetTree = Tree("jump_offsets(${arg.jumpOffsets.size})", jumpOffsetChildren)
                val children = listOf(defaultTree, lowTree, highTree, jumpOffsetTree)
                Tree("switch", children)
            }

            is ApiArgument.OpCodeValue -> {
                return Tree("${arg.name}(${arg.code.toHexString().uppercase()})")
            }
        }
    }

    private fun formatSignature(className: String, methodName: String, signature: Signature): String {
        val formattedReturnType = formatSignatureType(signature.returnType)
        val formattedArgumentList =
            signature.parameters?.joinToString(", ", "(", ")", transform = ::formatSignatureType) ?: ""
        return "$formattedReturnType $className.$methodName$formattedArgumentList"
    }

    private fun formatSignatureType(signatureType: SignatureType): String {
        val name = signatureType.name.replace("/", ".")
        val array = "[]".repeat(signatureType.dimensions)
        return name + array
    }

    fun formatAccessFlags(accessFlags: Set<AccessFlag>): String {
        return accessFlags.joinToString(", ", "[", "]") { it.displayName.uppercase() }
    }

    private fun hexUpper(x: Int): String = "0x" + Integer.toHexString(x).uppercase()
    private fun String.toSanitizedString(): String =
        this.toByteArray().toSanitizedString()

    private fun ByteArray.toSanitizedString(): String =
        this.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "?" }

    private fun List<Byte>.toSanitizedString(): String =
        this.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "?" }

    private fun List<Byte>.toHexString(): String = this.joinToString("", "0x") { it.toHex() }
    private fun Byte.toHex(): String = String.format("%02X", this)

}
