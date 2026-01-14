package com.seanshubin.jvmspec.domain.output.formatting

import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import com.seanshubin.jvmspec.domain.infrastructure.collections.Tree
import com.seanshubin.jvmspec.domain.model.api.*
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
        val toConstantTree = { constant: JvmConstant -> constantTree(constant) }
        val children = interfaces.map(toConstantTree)
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
            val tree = constantTree(constant)
            val hexIndex = "0x%04X".format(index.toInt())
            Tree("[$index($hexIndex)] ${tree.node}", tree.children)
        }
        val parent = Tree("constants(${children.size})", children)
        return parent
    }

    private fun constantTree(constant: JvmConstant): Tree {
        val tagLabel = "${constant.tag.name}_${constant.tag.id}"
        return when (constant) {
            is JvmConstant.Utf8 -> utf8ConstantTree(tagLabel, constant)
            is JvmConstant.IntegerConstant -> integerConstantTree(tagLabel, constant)
            is JvmConstant.FloatConstant -> floatConstantTree(tagLabel, constant)
            is JvmConstant.LongConstant -> longConstantTree(tagLabel, constant)
            is JvmConstant.DoubleConstant -> doubleConstantTree(tagLabel, constant)
            is JvmConstant.Class -> classConstantTree(tagLabel, constant)
            is JvmConstant.StringConstant -> stringConstantTree(tagLabel, constant)
            is JvmConstant.Module -> moduleConstantTree(tagLabel, constant)
            is JvmConstant.Package -> packageConstantTree(tagLabel, constant)
            is JvmConstant.NameAndType -> nameAndTypeConstantTree(tagLabel, constant)
            is JvmConstant.Ref -> refConstantTree(tagLabel, constant)
            is JvmConstant.MethodType -> methodTypeConstantTree(tagLabel, constant)
            is JvmConstant.MethodHandle -> methodHandleConstantTree(tagLabel, constant)
            is JvmConstant.Dynamic -> dynamicConstantTree(tagLabel, constant)
            else -> Tree("unknown-constant-type: ${constant::class.simpleName}")
        }
    }

    private fun utf8ConstantTree(tagLabel: String, constant: JvmConstant.Utf8): Tree =
        Tree(tagLabel, listOf(Tree(constant.value.toSanitizedString())))

    private fun integerConstantTree(tagLabel: String, constant: JvmConstant.IntegerConstant): Tree =
        Tree(tagLabel, listOf(Tree("${constant.value}")))

    private fun floatConstantTree(tagLabel: String, constant: JvmConstant.FloatConstant): Tree =
        Tree(tagLabel, listOf(Tree("${constant.value}")))

    private fun longConstantTree(tagLabel: String, constant: JvmConstant.LongConstant): Tree =
        Tree(tagLabel, listOf(Tree("${constant.value}")))

    private fun doubleConstantTree(tagLabel: String, constant: JvmConstant.DoubleConstant): Tree =
        Tree(tagLabel, listOf(Tree("${constant.value}")))

    private fun classConstantTree(tagLabel: String, constant: JvmConstant.Class): Tree =
        Tree(tagLabel, listOf(constantTree(constant.nameUtf8)))

    private fun stringConstantTree(tagLabel: String, constant: JvmConstant.StringConstant): Tree =
        Tree(tagLabel, listOf(constantTree(constant.valueUtf8)))

    private fun moduleConstantTree(tagLabel: String, constant: JvmConstant.Module): Tree =
        Tree(tagLabel, listOf(constantTree(constant.moduleNameUtf8)))

    private fun packageConstantTree(tagLabel: String, constant: JvmConstant.Package): Tree =
        Tree(tagLabel, listOf(constantTree(constant.packageNameUtf8)))

    private fun nameAndTypeConstantTree(tagLabel: String, constant: JvmConstant.NameAndType): Tree =
        Tree(tagLabel, listOf(constantTree(constant.nameUtf8), constantTree(constant.descriptorUtf8)))

    private fun refConstantTree(tagLabel: String, constant: JvmConstant.Ref): Tree =
        Tree(tagLabel, listOf(constantTree(constant.jvmClass), constantTree(constant.jvmNameAndType)))

    private fun methodTypeConstantTree(tagLabel: String, constant: JvmConstant.MethodType): Tree =
        Tree(tagLabel, listOf(constantTree(constant.descriptorUtf8)))

    private fun methodHandleConstantTree(tagLabel: String, constant: JvmConstant.MethodHandle): Tree =
        Tree(
            tagLabel, listOf(
                Tree("${constant.referenceKind.name}(${constant.referenceKind.code})"),
                constantTree(constant.reference)
            )
        )

    private fun dynamicConstantTree(tagLabel: String, constant: JvmConstant.Dynamic): Tree =
        Tree(
            tagLabel, listOf(
                Tree("bootstrap-method: ${constant.bootstrapMethodAttrIndex}"),
                constantTree(constant.nameAndType)
            )
        )

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
        val toInstructionTree = { instruction: JvmInstruction -> instructionTree(instruction) }
        val instructionsChildren = codeAttribute.instructions().map(toInstructionTree)
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
        val toExceptionTableTree = { exception: JvmExceptionTable -> exceptionTableTree(exception) }
        val children = exceptions.map(toExceptionTableTree)
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
            is JvmArgument.Constant -> constantArgTree(arg)
            is JvmArgument.IntValue -> intValueArgTree(arg)
            is JvmArgument.ArrayTypeValue -> arrayTypeValueArgTree(arg)
            is JvmArgument.LookupSwitch -> lookupSwitchArgTree(arg)
            is JvmArgument.TableSwitch -> tableSwitchArgTree(arg)
            is JvmArgument.OpCodeValue -> opCodeValueArgTree(arg)
        }
    }

    private fun constantArgTree(arg: JvmArgument.Constant): Tree =
        constantTree(arg.value)

    private fun intValueArgTree(arg: JvmArgument.IntValue): Tree =
        Tree(arg.value.toString())

    private fun arrayTypeValueArgTree(arg: JvmArgument.ArrayTypeValue): Tree =
        Tree("${arg.value.name}(${arg.value.code})")

    private fun lookupSwitchArgTree(arg: JvmArgument.LookupSwitch): Tree {
        val default = Tree("default: ${arg.default.toString()}")
        val toPairTree = { (match, offset): Pair<Int, Int> -> Tree("$match:$offset") }
        val pairs = arg.lookup.map(toPairTree)
        val pairsTree = Tree("pairs(${pairs.size})", pairs)
        val children = listOf(default) + pairsTree
        return Tree("switch", children)
    }

    private fun tableSwitchArgTree(arg: JvmArgument.TableSwitch): Tree {
        val defaultTree = Tree("default", listOf(Tree(arg.default.toString())))
        val lowTree = Tree("low", listOf(Tree(arg.low.toString())))
        val highTree = Tree("high", listOf(Tree(arg.high.toString())))
        val jumpOffsetChildren = arg.jumpOffsets.map { Tree(it.toString()) }
        val jumpOffsetTree = Tree("jump_offsets(${arg.jumpOffsets.size})", jumpOffsetChildren)
        val children = listOf(defaultTree, lowTree, highTree, jumpOffsetTree)
        return Tree("switch", children)
    }

    private fun opCodeValueArgTree(arg: JvmArgument.OpCodeValue): Tree =
        Tree("${arg.name}(${arg.code.toHexString().uppercase()})")

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
