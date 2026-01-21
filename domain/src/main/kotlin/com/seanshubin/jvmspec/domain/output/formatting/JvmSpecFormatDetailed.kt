package com.seanshubin.jvmspec.domain.output.formatting

import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import com.seanshubin.jvmspec.domain.infrastructure.collections.Tree
import com.seanshubin.jvmspec.domain.model.api.*
import java.util.*

class JvmSpecFormatDetailed : JvmSpecFormat {
    override fun classTreeList(jvmClass: JvmClass): List<Tree> {
        val minorHex = String.format("0x%04X", jvmClass.minorVersion.toInt())
        val majorHex = String.format("0x%04X", jvmClass.majorVersion.toInt())
        return listOf(
            Tree("origin: ${jvmClass.origin}"),
            Tree("magic: ${hexUpper(jvmClass.magic)}"),
            Tree("minor_version: ${jvmClass.minorVersion}($minorHex)"),
            Tree("major_version: ${jvmClass.majorVersion}($majorHex)"),
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

    private fun utf8ConstantTree(tagLabel: String, constant: JvmConstant.Utf8): Tree {
        val sanitized = constant.value.toSanitizedString()
        return Tree("$tagLabel: $sanitized")
    }

    private fun integerConstantTree(tagLabel: String, constant: JvmConstant.IntegerConstant): Tree {
        val hex = String.format("0x%08X", constant.value)
        return Tree("$tagLabel: ${constant.value} ($hex)")
    }

    private fun floatConstantTree(tagLabel: String, constant: JvmConstant.FloatConstant): Tree {
        val hex = String.format("0x%08X", constant.value.toRawBits())
        return Tree("$tagLabel: ${constant.value} ($hex)")
    }

    private fun longConstantTree(tagLabel: String, constant: JvmConstant.LongConstant): Tree {
        val hex = String.format("0x%016X", constant.value)
        return Tree("$tagLabel: ${constant.value} ($hex)")
    }

    private fun doubleConstantTree(tagLabel: String, constant: JvmConstant.DoubleConstant): Tree {
        val hex = String.format("0x%016X", constant.value.toRawBits())
        return Tree("$tagLabel: ${constant.value} ($hex)")
    }

    private fun classConstantTree(tagLabel: String, constant: JvmConstant.Class): Tree {
        val className = (constant.nameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree("$tagLabel: $className", listOf(constantTree(constant.nameUtf8)))
    }

    private fun stringConstantTree(tagLabel: String, constant: JvmConstant.StringConstant): Tree {
        val stringValue = (constant.valueUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree("$tagLabel: $stringValue", listOf(constantTree(constant.valueUtf8)))
    }

    private fun moduleConstantTree(tagLabel: String, constant: JvmConstant.Module): Tree {
        val moduleName = (constant.moduleNameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree("$tagLabel: $moduleName", listOf(constantTree(constant.moduleNameUtf8)))
    }

    private fun packageConstantTree(tagLabel: String, constant: JvmConstant.Package): Tree {
        val packageName = (constant.packageNameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree("$tagLabel: $packageName", listOf(constantTree(constant.packageNameUtf8)))
    }

    private fun nameAndTypeConstantTree(tagLabel: String, constant: JvmConstant.NameAndType): Tree {
        val name = (constant.nameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        val descriptor = (constant.descriptorUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree(
            "$tagLabel: $name:$descriptor",
            listOf(constantTree(constant.nameUtf8), constantTree(constant.descriptorUtf8))
        )
    }

    private fun refConstantTree(tagLabel: String, constant: JvmConstant.Ref): Tree {
        val className = (constant.jvmClass as? JvmConstant.Class)?.let {
            (it.nameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString()
        } ?: "?"
        val nameAndType = (constant.jvmNameAndType as? JvmConstant.NameAndType)?.let {
            val name = (it.nameUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
            val descriptor = (it.descriptorUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
            "$name:$descriptor"
        } ?: "?"
        return Tree(
            "$tagLabel: $className.$nameAndType",
            listOf(constantTree(constant.jvmClass), constantTree(constant.jvmNameAndType))
        )
    }

    private fun methodTypeConstantTree(tagLabel: String, constant: JvmConstant.MethodType): Tree {
        val descriptor = (constant.descriptorUtf8 as? JvmConstant.Utf8)?.value?.toSanitizedString() ?: "?"
        return Tree("$tagLabel: $descriptor", listOf(constantTree(constant.descriptorUtf8)))
    }

    private fun methodHandleConstantTree(tagLabel: String, constant: JvmConstant.MethodHandle): Tree {
        val kindName = constant.referenceKind.name
        val kindCode = constant.referenceKind.code
        return Tree(
            "$tagLabel: $kindName($kindCode)", listOf(
                Tree("reference_kind: $kindName($kindCode)"),
                constantTree(constant.reference)
            )
        )
    }

    private fun dynamicConstantTree(tagLabel: String, constant: JvmConstant.Dynamic): Tree {
        val index = constant.bootstrapMethodAttrIndex
        val hexIndex = String.format("0x%04X", index.toInt())
        return Tree(
            "$tagLabel: bootstrap=$index($hexIndex)", listOf(
                Tree("bootstrap_method_attr_index: $index($hexIndex)"),
                constantTree(constant.nameAndType)
            )
        )
    }

    private fun fieldOrMethodTree(caption: String, index: Int, fieldOrMethod: JvmFieldOrMethod): Tree {
        val hexIndex = String.format("0x%04X", index)
        val formattedSignature = fieldOrMethod.signature().javaFormat()
        val children = listOf(
            Tree("access_flags: ${formatAccessFlags(fieldOrMethod.accessFlags())}"),
            Tree("signature: $formattedSignature"),
            attributesTree(fieldOrMethod.attributes())
        )
        val parent = Tree("$caption[$index($hexIndex)]: $formattedSignature", children)
        return parent
    }

    private fun attributesTree(attributes: List<JvmAttribute>): Tree {
        val children = attributes.mapIndexed { index, attribute -> attributeTree(index, attribute) }
        val parent = Tree("attributes(${children.size})", children)
        return parent
    }

    private fun attributeTree(index: Int, attribute: JvmAttribute): Tree {
        val hexIndex = String.format("0x%04X", index)
        val name = attribute.name()
        val bytesNode = listOf(
            Tree(attribute.bytes().toHexString()),
            Tree(attribute.bytes().toSanitizedString())
        )
        val children = listOf(
            Tree("name: $name"),
            Tree("bytes(${attribute.bytes().size})", bytesNode),
        )
        val codeList = if (attribute is JvmCodeAttribute) {
            listOf(codeTree(attribute))
        } else {
            emptyList()
        }
        return Tree("attribute[$index($hexIndex)]: $name", children + codeList)
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
        val children = exceptions.mapIndexed { index, exception -> exceptionTableTree(index, exception) }
        val parent = Tree("exception_table(${children.size})", children)
        return parent
    }

    private fun exceptionTableTree(index: Int, exception: JvmExceptionTable): Tree {
        val hexIndex = String.format("0x%04X", index)
        val startPc = exception.startProgramCounter.formatDecimalHex()
        val endPc = exception.endProgramCounter.formatDecimalHex()
        val handlerPc = exception.handlerProgramCounter.formatDecimalHex()
        val children = listOf(
            Tree("start_pc: $startPc"),
            Tree("end_pc: $endPc"),
            Tree("handler_pc: $handlerPc"),
            Tree("catch_type: ${exception.catchType}")
        )
        return Tree("exception[$index($hexIndex)]", children)
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

    private fun intValueArgTree(arg: JvmArgument.IntValue): Tree {
        val value = arg.value
        val hex = when {
            value >= -128 && value <= 255 -> String.format("0x%02X", value and 0xFF)
            value >= -32768 && value <= 65535 -> String.format("0x%04X", value and 0xFFFF)
            else -> String.format("0x%08X", value)
        }
        return Tree("$value($hex)")
    }

    private fun arrayTypeValueArgTree(arg: JvmArgument.ArrayTypeValue): Tree {
        val hex = String.format("0x%02X", arg.value.code)
        return Tree("${arg.value.name}($hex)")
    }

    private fun lookupSwitchArgTree(arg: JvmArgument.LookupSwitch): Tree {
        val defaultHex = String.format("0x%08X", arg.default)
        val default = Tree("default: ${arg.default}($defaultHex)")
        val toPairTree = { (match, offset): Pair<Int, Int> ->
            val matchHex = String.format("0x%08X", match)
            val offsetHex = String.format("0x%08X", offset)
            Tree("match=$match($matchHex) offset=$offset($offsetHex)")
        }
        val pairs = arg.lookup.map(toPairTree)
        val pairsTree = Tree("pairs(${pairs.size})", pairs)
        val children = listOf(default, pairsTree)
        return Tree("lookupswitch", children)
    }

    private fun tableSwitchArgTree(arg: JvmArgument.TableSwitch): Tree {
        val defaultHex = String.format("0x%08X", arg.default)
        val lowHex = String.format("0x%08X", arg.low)
        val highHex = String.format("0x%08X", arg.high)
        val defaultTree = Tree("default: ${arg.default}($defaultHex)")
        val lowTree = Tree("low: ${arg.low}($lowHex)")
        val highTree = Tree("high: ${arg.high}($highHex)")
        val jumpOffsetChildren = arg.jumpOffsets.map {
            val hex = String.format("0x%08X", it)
            Tree("$it($hex)")
        }
        val jumpOffsetTree = Tree("jump_offsets(${arg.jumpOffsets.size})", jumpOffsetChildren)
        val children = listOf(defaultTree, lowTree, highTree, jumpOffsetTree)
        return Tree("tableswitch", children)
    }

    private fun opCodeValueArgTree(arg: JvmArgument.OpCodeValue): Tree {
        val hex = arg.code.toHexString().uppercase()
        return Tree("${arg.name}(0x$hex)")
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
