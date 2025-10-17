package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import com.seanshubin.jvmspec.domain.tree.Tree

class JvmSpecFormatDetailed : JvmSpecFormat {
    override fun treeList(apiClass: ApiClass): List<Tree> {
        return listOf(
            Tree("origin: ${apiClass.origin()}"),
            Tree("magic: ${hexUpper(apiClass.magic())}"),
            Tree("minor version: ${apiClass.minorVersion()}"),
            Tree("major version: ${apiClass.majorVersion()}"),
            accessFlagsTree(apiClass.accessFlags()),
            Tree("this_class: ${apiClass.thisClassName()}"),
            Tree("super_class: ${apiClass.superClassName()}"),
            constantsTree(apiClass.constants()),
            interfacesTree(apiClass.interfaces()),
            fieldsTree(apiClass.fields()),
            methodsTree(apiClass.methods()),
            attributesTree(apiClass.attributes())
        )
    }

    private fun methodsTree(methods: List<ApiMethod>): Tree {
        val children = methods.mapIndexed { index, method -> tree("method", method, index) }
        val parent = Tree("methods(${children.size})", children)
        return parent
    }

    private fun fieldsTree(methods: List<ApiField>): Tree {
        val children = methods.mapIndexed { index, field -> tree("field", field, index) }
        val parent = Tree("field(${children.size})", children)
        return parent
    }

    private fun interfacesTree(interfaces: List<ApiConstant.Constant>): Tree {
        val children = interfaces.map { constant ->
            tree(constant)
        }
        val parent = Tree("interfaces(${children.size})", children)
        return parent
    }

    private fun accessFlagsTree(accessFlags: Set<AccessFlag>): Tree {
        val accessFlagList = accessFlags.map { it.displayName }.toList().sorted()
        val accessFlagString = accessFlagList.joinToString(" ")
        val accessFlagTree = Tree("access_flags: $accessFlagString")
        return accessFlagTree
    }

    private fun constantsTree(constants: List<ApiConstant.Constant>): Tree {
        val children = constants.map { constant ->
            tree(constant)
        }
        val parent = Tree("constants(${children.size})", children)
        return parent
    }

    private fun tree(constant: ApiConstant): Tree {
        return when (constant) {
            is ApiConstant.Constant -> {
                val parent = "[${constant.index}] ${constant.tagName}(${constant.tagId})"
                val children = constant.parts.map { part -> tree(part) }
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

    private fun tree(caption: String, fieldOrMethod: ApiFieldOrMethod, index: Int): Tree {
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
        val children = attributes.mapIndexed { index, attribute -> tree(attribute, index) }
        val parent = Tree("attributes(${children.size})", children)
        return parent
    }

    private fun tree(attribute: ApiAttribute, index: Int): Tree {
        val bytesNode = listOf(
            Tree(attribute.bytes().toHexString()),
            Tree(attribute.bytes().toSanitizedString())
        )
        val children = listOf(
            Tree("name: ${attribute.name()}"),
            Tree("bytes(${attribute.bytes().size})", bytesNode),
        )
        return Tree("attribute[$index]", children)
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
        return accessFlags.joinToString(", ") { it.displayName }
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
