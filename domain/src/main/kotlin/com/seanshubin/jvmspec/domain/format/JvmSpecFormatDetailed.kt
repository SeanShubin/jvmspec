package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.jvm.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import com.seanshubin.jvmspec.domain.tree.Tree
import java.util.*

class JvmSpecFormatDetailed : JvmSpecFormat {
    override fun classTreeList(jvmClass: JvmClass): List<Tree> {
        return listOf(
            Tree("origin: ${jvmClass.origin}"),
            Tree("magic: ${hexUpper(jvmClass.magic)}"),
            Tree("minor version: ${jvmClass.minorVersion}"),
            Tree("major version: ${jvmClass.majorVersion}"),
            accessFlagsTree(jvmClass.accessFlags),
            Tree("this_class: ${jvmClass.thisClassName}"),
            Tree("super_class: ${jvmClass.superClassName}"),
            constantsTree(jvmClass.constants),
            interfacesTree(jvmClass.interfaces()),
            fieldsTree(jvmClass.fields()),
            methodsTree(jvmClass.methods()),
            attributesTree(jvmClass.attributes())
        )
    }

    private fun methodsTree(methods: List<JvmMethod>): Tree {
        val children = methods.mapIndexed { index, method -> fieldOrMethodTree("method", index, method) }
        val parent = Tree("methods(${children.size})", children)
        return parent
    }

    private fun fieldsTree(methods: List<JvmField>): Tree {
        val children = methods.mapIndexed { index, field -> fieldOrMethodTree("field", index, field) }
        val parent = Tree("fields(${children.size})", children)
        return parent
    }

    private fun interfacesTree(interfaces: List<JvmConstant>): Tree {
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

    private fun constantsTree(constants: SortedMap<UShort, JvmConstant>): Tree {
        val children = constants.map { (index, constant) ->
            constantTree(constant)
        }
        val parent = Tree("constants(${children.size})", children)
        return parent
    }

    private fun constantTree(constant: JvmConstant): Tree {
        return when (constant) {
            is JvmConstant.Utf8 -> {
                Tree(constant.value.toSanitizedString())
            }

            is JvmConstant.IntegerConstant -> {
                Tree("${constant.value}")
            }

            is JvmConstant.FloatConstant -> {
                Tree("${constant.value}")
            }

            is JvmConstant.LongConstant -> {
                Tree("${constant.value}")
            }

            is JvmConstant.DoubleConstant -> {
                Tree("${constant.value}")
            }

            is JvmConstant.Class -> {
                Tree("class", listOf(constantTree(constant.nameUtf8)))
            }

            is JvmConstant.StringConstant -> {
                Tree("string", listOf(constantTree(constant.valueUtf8)))
            }

            is JvmConstant.Module -> {
                Tree("module", listOf(constantTree(constant.moduleNameUtf8)))
            }

            is JvmConstant.Package -> {
                Tree("package", listOf(constantTree(constant.packageNameUtf8)))
            }

            is JvmConstant.NameAndType -> {
                Tree(
                    "name-and-type", listOf(
                        constantTree(constant.nameUtf8),
                        constantTree(constant.descriptorUtf8)
                    )
                )
            }

            is JvmConstant.Ref -> {
                Tree(
                    constant.tag.name.lowercase(), listOf(
                        constantTree(constant.jvmClass),
                        constantTree(constant.jvmNameAndType)
                    )
                )
            }

            is JvmConstant.MethodType -> {
                Tree("method-type", listOf(constantTree(constant.descriptorUtf8)))
            }

            is JvmConstant.MethodHandle -> {
                Tree(
                    "method-handle", listOf(
                        Tree("${constant.referenceKind.name}(${constant.referenceKind.code})"),
                        constantTree(constant.reference)
                    )
                )
            }

            is JvmConstant.Dynamic -> {
                Tree(
                    constant.tag.name.lowercase(), listOf(
                        Tree("bootstrap-method: ${constant.bootstrapMethodAttrIndex}"),
                        constantTree(constant.nameAndType)
                    )
                )
            }

            else -> Tree("unknown-constant-type: ${constant::class.simpleName}")
        }
    }

    private fun fieldOrMethodTree(caption: String, index: Int, fieldOrMethod: JvmFieldOrMethod): Tree {
        val formattedSignature = fieldOrMethod.signature().javaFormat()
        val children = listOf(
            Tree("access flags = ${formatAccessFlags(fieldOrMethod.accessFlags())}"),
            Tree("signature = $formattedSignature"),
            attributesTree(fieldOrMethod.attributes())
        )
        val parent = Tree("$caption[$index]", children)
        return parent
    }

    private fun attributesTree(attributes: List<JvmAttribute>): Tree {
        val children = attributes.mapIndexed { index, attribute -> attributeTree(index, attribute) }
        val parent = Tree("attributes(${children.size})", children)
        return parent
    }

    private fun attributeTree(index: Int, attribute: JvmAttribute): Tree {
        val bytesNode = listOf(
            Tree(attribute.bytes().toHexString()),
            Tree(attribute.bytes().toSanitizedString())
        )
        val children = listOf(
            Tree("name: ${attribute.name()}"),
            Tree("bytes(${attribute.bytes().size})", bytesNode),
        )
        val codeList = if (attribute is JvmCodeAttribute) {
            listOf(codeTree(attribute))
        } else {
            emptyList()
        }
        return Tree("attribute[$index]", children + codeList)
    }

    private fun codeTree(codeAttribute: JvmCodeAttribute): Tree {
        val instructionsChildren = codeAttribute.instructions().map { instruction ->
            instructionTree(instruction)
        }
        val maxStackTree = Tree("maxStack: ${codeAttribute.maxStack.formatDecimalHex()}")
        val maxLocalsTree = Tree("maxLocals: ${codeAttribute.maxLocals.formatDecimalHex()}")
        val codeLengthTree = Tree("codeLength: ${codeAttribute.codeLength.formatDecimalHex()}")
        val attributesTree = attributesTree(codeAttribute.attributes())
        val instructionsTree = Tree("instructions(${instructionsChildren.size})", instructionsChildren)
        val exceptionTableTree = exceptionTableListTree(codeAttribute.exceptionTable())
        return Tree(
            "code", listOf(
                maxStackTree, maxLocalsTree, codeLengthTree,
                instructionsTree, exceptionTableTree, attributesTree
            )
        )
    }

    private fun exceptionTableListTree(exceptions: List<JvmExceptionTable>): Tree {
        val children = exceptions.map { exception ->
            exceptionTableTree(exception)
        }
        val parent = Tree("exceptions(${children.size})", children)
        return parent
    }

    private fun exceptionTableTree(exception: JvmExceptionTable): Tree {
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

    override fun instructionTree(jvmInstruction: JvmInstruction): Tree {
        val argsTreeList = argsTreeList(jvmInstruction.args())
        val name = jvmInstruction.name()
        val code = jvmInstruction.code()
        val bytes = jvmInstruction.bytes()
        val bytesAsHex = bytes.toHexString()
        val children = listOf(Tree(bytesAsHex)) + argsTreeList
        val parent = Tree("$name(0x${code.toHexString().uppercase()})", children)
        return parent
    }

    private fun argsTreeList(argList: List<JvmArgument>): List<Tree> {
        return argList.map(::argsTree)
    }

    private fun argsTree(arg: JvmArgument): Tree {
        return when (arg) {
            is JvmArgument.Constant -> {
                constantTree(arg.value)
            }

            is JvmArgument.IntValue -> {
                Tree(arg.value.toString())
            }

            is JvmArgument.ArrayTypeValue -> {
                Tree("${arg.value.name}(${arg.value.code})")
            }

            is JvmArgument.LookupSwitch -> {
                val default = Tree("default: ${arg.default.toString()}")
                val pairs = arg.lookup.map { (match, offset) ->
                    Tree("$match:$offset")
                }
                val pairsTree = Tree("pairs(${pairs.size})", pairs)
                val children = listOf(default) + pairsTree
                Tree("switch", children)
            }

            is JvmArgument.TableSwitch -> {
                val defaultTree = Tree("default", listOf(Tree(arg.default.toString())))
                val lowTree = Tree("low", listOf(Tree(arg.low.toString())))
                val highTree = Tree("high", listOf(Tree(arg.high.toString())))
                val jumpOffsetChildren = arg.jumpOffsets.map { Tree(it.toString()) }
                val jumpOffsetTree = Tree("jump_offsets(${arg.jumpOffsets.size})", jumpOffsetChildren)
                val children = listOf(defaultTree, lowTree, highTree, jumpOffsetTree)
                Tree("switch", children)
            }

            is JvmArgument.OpCodeValue -> {
                return Tree("${arg.name}(${arg.code.toHexString().uppercase()})")
            }
        }
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
    private fun UShort.formatDecimalHex(): String {
        val decimal = this.toInt()
        val hex = String.format("%04X", decimal)
        return "$decimal(0x$hex)"
    }
    private fun Int.formatDecimalHex(): String {
        val decimal = this
        val hex = String.format("%08X", decimal)
        return "$decimal(0x$hex)"
    }
    private fun ConstantPoolTag.formatNameId(): String {
        return "${name}_$id"
    }
}
