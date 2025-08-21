package com.seanshubin.jvmspec.domain.data

enum class AccessFlag(val mask: UShort) {
    ACC_PUBLIC(0x0001u),
    ACC_PRIVATE(0x0002u),
    ACC_PROTECTED(0x0004u),
    ACC_STATIC(0x0008u),
    ACC_FINAL(0x0010u),
    ACC_SUPER(0x0020u),
    ACC_VOLATILE(0x0040u),
    ACC_TRANSIENT(0x0080u),
    ACC_INTERFACE(0x0200u),
    ACC_ABSTRACT(0x0400u),
    ACC_SYNTHETIC(0x1000u),
    ACC_ANNOTATION(0x2000u),
    ACC_ENUM(0x4000u),
    ACC_MODULE(0x8000u);

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
