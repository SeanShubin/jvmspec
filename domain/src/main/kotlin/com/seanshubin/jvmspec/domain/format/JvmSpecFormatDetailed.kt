package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import com.seanshubin.jvmspec.domain.tree.Tree

class JvmSpecFormatDetailed : JvmSpecFormat {
    override fun treeList(apiClass: ApiClass): List<Tree> {
        val originNode = Tree("origin: ${apiClass.origin()}")
        val magicNode = Tree("magic: ${hexUpper(apiClass.magic())}")
        val minorVersionNode = Tree("minor version: ${apiClass.minorVersion()}")
        val majorVersionNode = Tree("major version: ${apiClass.majorVersion()}")
        val constants = apiClass.constants()
        val constantChildren = constants.map { constant ->
            tree(constant)
        }
        val accessFlagsString = accessFlagsString(apiClass.accessFlags())
        val accessFlagsNode = Tree("access_flags: $accessFlagsString")
        val thisClassName = apiClass.thisClassName()
        val thisClassNode = Tree("this_class: $thisClassName")
        val superClassName = apiClass.superClassName()
        val superClassNode = Tree("super_class: $superClassName")
        val constantsNode = Tree("constants(${constantChildren.size})", constantChildren)
        val interfaceChildren = apiClass.interfaces().map { tree(it) }
        val interfaceNode = Tree("interfaces(${interfaceChildren.size})", interfaceChildren)
        val fieldChildren = apiClass.fields().mapIndexed { index, field -> tree(field, index) }
        val fieldNode = Tree("fields(${fieldChildren.size})", fieldChildren)

        return listOf(
            originNode,
            magicNode,
            minorVersionNode,
            majorVersionNode,
            accessFlagsNode,
            thisClassNode,
            superClassNode,
            constantsNode,
            interfaceNode,
            fieldNode
        )
//        val fieldsCount: UShort,
//        val fields: List<FieldInfo>,
//        val methodsCount: UShort,
//        val methods: List<MethodInfo>,
//        val attributesCount: UShort,
//        val attributes: List<AttributeInfo>
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

    private fun tree(field: ApiField, index: Int): Tree {
        val formattedSignature = formatSignature(
            field.className().replace("/", "."),
            field.name(),
            field.signature()
        )
        val children = listOf(
            Tree("access flags = ${field.accessFlags()}"),
            Tree("signature = $formattedSignature")
        )
        val parent = Tree("field[$index]", children)
        return parent
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

    fun accessFlagsString(accessFlags: Set<AccessFlag>): String {
        return accessFlags.joinToString(", ") { it.displayName }
    }

    private fun hexUpper(x: Int): String = "0x" + Integer.toHexString(x).uppercase()
    private fun String.toSanitizedString(): String =
        this.toByteArray().toSanitizedString()

    private fun ByteArray.toSanitizedString(): String =
        this.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "." }

}
