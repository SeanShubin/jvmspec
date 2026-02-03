package com.seanshubin.jvmspec.classfile.specification

enum class AccessFlag(val mask: UShort, val displayName: String) {
    ACC_PUBLIC(0x0001u, "public"),
    ACC_PRIVATE(0x0002u, "private"),
    ACC_PROTECTED(0x0004u, "protected"),
    ACC_STATIC(0x0008u, "static"),
    ACC_FINAL(0x0010u, "final"),
    ACC_SUPER(0x0020u, "super"),
    ACC_VOLATILE(0x0040u, "volatile"),
    ACC_TRANSIENT(0x0080u, "transient"),
    ACC_INTERFACE(0x0200u, "interface"),
    ACC_ABSTRACT(0x0400u, "abstract"),
    ACC_SYNTHETIC(0x1000u, "synthetic"),
    ACC_ANNOTATION(0x2000u, "annotation"),
    ACC_ENUM(0x4000u, "enum"),
    ACC_MODULE(0x8000u, "module");

    companion object {
        val uShortZero = 0u.toUShort()
        fun fromMask(mask: UShort): Set<AccessFlag> {
            return entries.filter { (mask and it.mask) != uShortZero }.toSet()
        }

        fun toMask(flags: Set<AccessFlag>): UShort {
            return flags.fold(uShortZero) { acc, flag -> acc or flag.mask }
        }
    }
}