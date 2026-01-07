package com.seanshubin.jvmspec.domain.prototype

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import com.seanshubin.jvmspec.domain.primitive.ReferenceKind

interface JvmConstant {
    val tag: ConstantPoolTag

    interface JvmConstantClass : JvmConstant {
        val name: JvmConstantUtf8
    }

    interface JvmConstantNameAndType : JvmConstant {
        val name: JvmConstantUtf8
        val descriptor: JvmConstantUtf8
    }

    interface JvmConstantRef : JvmConstant {
        val jvmClass: JvmConstantClass
        val jvmNameAndType: JvmConstantNameAndType
    }

    interface JvmConstantUtf8 : JvmConstant {
        val value: String
    }

    interface JvmConstantString : JvmConstant {
        val value: JvmConstantUtf8
    }

    interface JvmConstantInteger : JvmConstant {
        val value: Int
    }

    interface JvmConstantFloat : JvmConstant {
        val value: Float
    }

    interface JvmConstantLong : JvmConstant {
        val value: Long
    }

    interface JvmConstantDouble : JvmConstant {
        val value: Double
    }

    interface JvmConstantMethodHandle : JvmConstant {
        val referenceKind: ReferenceKind
        val reference: JvmConstantRef
    }

    interface JvmConstantMethodType : JvmConstant {
        val descriptor: JvmConstantUtf8
    }

    interface JvmConstantDynamic : JvmConstant {
        val bootstrapMethodAttrIndex: Int
        val nameAndType: JvmConstantNameAndType
    }

    interface JvmConstantModule : JvmConstant {
        val moduleName: JvmConstantUtf8
    }

    interface JvmConstantPackage : JvmConstant {
        val packageName: JvmConstantUtf8
    }

    // Data class implementations
    data class Utf8(
        override val tag: ConstantPoolTag,
        override val value: String
    ) : JvmConstantUtf8

    data class IntegerConstant(
        override val tag: ConstantPoolTag,
        override val value: Int
    ) : JvmConstantInteger

    data class FloatConstant(
        override val tag: ConstantPoolTag,
        override val value: Float
    ) : JvmConstantFloat

    data class LongConstant(
        override val tag: ConstantPoolTag,
        override val value: Long
    ) : JvmConstantLong

    data class DoubleConstant(
        override val tag: ConstantPoolTag,
        override val value: Double
    ) : JvmConstantDouble

    data class Class(
        override val tag: ConstantPoolTag,
        override val name: JvmConstantUtf8
    ) : JvmConstantClass

    data class StringConstant(
        override val tag: ConstantPoolTag,
        override val value: JvmConstantUtf8
    ) : JvmConstantString

    data class Module(
        override val tag: ConstantPoolTag,
        override val moduleName: JvmConstantUtf8
    ) : JvmConstantModule

    data class Package(
        override val tag: ConstantPoolTag,
        override val packageName: JvmConstantUtf8
    ) : JvmConstantPackage

    data class NameAndType(
        override val tag: ConstantPoolTag,
        override val name: JvmConstantUtf8,
        override val descriptor: JvmConstantUtf8
    ) : JvmConstantNameAndType

    data class Ref(
        override val tag: ConstantPoolTag,
        override val jvmClass: JvmConstantClass,
        override val jvmNameAndType: JvmConstantNameAndType
    ) : JvmConstantRef

    data class MethodType(
        override val tag: ConstantPoolTag,
        override val descriptor: JvmConstantUtf8
    ) : JvmConstantMethodType

    data class MethodHandle(
        override val tag: ConstantPoolTag,
        override val referenceKind: ReferenceKind,
        override val reference: JvmConstantRef
    ) : JvmConstantMethodHandle

    data class Dynamic(
        override val tag: ConstantPoolTag,
        override val bootstrapMethodAttrIndex: Int,
        override val nameAndType: JvmConstantNameAndType
    ) : JvmConstantDynamic
}
