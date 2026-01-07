package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import com.seanshubin.jvmspec.domain.primitive.ReferenceKind

interface JvmConstant {
    val tag: ConstantPoolTag

    interface JvmConstantClass : JvmConstant {
        val nameUtf8: JvmConstantUtf8
        val name: String
    }

    interface JvmConstantNameAndType : JvmConstant {
        val nameUtf8: JvmConstantUtf8
        val descriptorUtf8: JvmConstantUtf8
        val name: String
        val descriptor: String
    }

    interface JvmConstantRef : JvmConstant {
        val jvmClass: JvmConstantClass
        val className: String
        val jvmNameAndType: JvmConstantNameAndType
    }

    interface JvmConstantUtf8 : JvmConstant {
        val value: String
    }

    interface JvmConstantString : JvmConstant {
        val valueUtf8: JvmConstantUtf8
        val value: String
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
        val descriptorUtf8: JvmConstantUtf8
        val descriptor: String
    }

    interface JvmConstantDynamic : JvmConstant {
        val bootstrapMethodAttrIndex: Int
        val nameAndType: JvmConstantNameAndType
    }

    interface JvmConstantModule : JvmConstant {
        val moduleNameUtf8: JvmConstantUtf8
        val moduleName: String
    }

    interface JvmConstantPackage : JvmConstant {
        val packageNameUtf8: JvmConstantUtf8
        val packageName: String
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
        override val nameUtf8: JvmConstantUtf8
    ) : JvmConstantClass {
        override val name: String get() = nameUtf8.value
    }

    data class StringConstant(
        override val tag: ConstantPoolTag,
        override val valueUtf8: JvmConstantUtf8
    ) : JvmConstantString {
        override val value: String get() = valueUtf8.value
    }

    data class Module(
        override val tag: ConstantPoolTag,
        override val moduleNameUtf8: JvmConstantUtf8
    ) : JvmConstantModule {
        override val moduleName: String get() = moduleNameUtf8.value
    }

    data class Package(
        override val tag: ConstantPoolTag,
        override val packageNameUtf8: JvmConstantUtf8
    ) : JvmConstantPackage {
        override val packageName: String get() = packageNameUtf8.value
    }

    data class NameAndType(
        override val tag: ConstantPoolTag,
        override val nameUtf8: JvmConstantUtf8,
        override val descriptorUtf8: JvmConstantUtf8
    ) : JvmConstantNameAndType {
        override val name: String get() = nameUtf8.value
        override val descriptor: String get() = descriptorUtf8.value
    }

    data class Ref(
        override val tag: ConstantPoolTag,
        override val jvmClass: JvmConstantClass,
        override val jvmNameAndType: JvmConstantNameAndType
    ) : JvmConstantRef {
        override val className: String get() = jvmClass.name
    }

    data class MethodType(
        override val tag: ConstantPoolTag,
        override val descriptorUtf8: JvmConstantUtf8
    ) : JvmConstantMethodType {
        override val descriptor: String get() = descriptorUtf8.value
    }

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
