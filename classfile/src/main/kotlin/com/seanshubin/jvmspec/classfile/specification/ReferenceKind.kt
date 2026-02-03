package com.seanshubin.jvmspec.classfile.specification

enum class ReferenceKind(val code: UByte) {
    GET_FIELD(1u),
    GET_STATIC(2u),
    PUT_FIELD(3u),
    PUT_STATIC(4u),
    INVOKE_VIRTUAL(5u),
    INVOKE_STATIC(6u),
    INVOKE_SPECIAL(7u),
    NEW_INVOKE_SPECIAL(8u),
    INVOKE_INTERFACE(9u);

    fun line(): String = "${this.name}($code)"

    companion object {
        fun fromCode(code: UByte): ReferenceKind {
            return entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("No ReferenceKind with code $code")
        }
    }
}