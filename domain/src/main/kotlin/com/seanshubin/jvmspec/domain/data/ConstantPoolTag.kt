package com.seanshubin.jvmspec.domain.data

enum class ConstantPoolTag(val id: UByte, val entriesTaken: Int) {
    UTF8(1u, 1),
    INTEGER(3u, 1),
    FLOAT(4u, 1),
    LONG(5u, 2),
    DOUBLE(6u, 2),
    CLASS(7u, 1),
    STRING(8u, 1),
    FIELD_REF(9u, 1),
    METHOD_REF(10u, 1),
    INTERFACE_METHOD_REF(11u, 1),
    NAME_AND_TYPE(12u, 1),
    METHOD_HANDLE(15u, 1),
    METHOD_TYPE(16u, 1),
    DYNAMIC(17u, 1),
    INVOKE_DYNAMIC(18u, 1),
    MODULE(19u, 1),
    PACKAGE(20u, 1);

    fun line(): String {
        return "$name($id)"
    }

    companion object {
        fun fromId(id: UByte): ConstantPoolTag {
            return entries.firstOrNull { it.id == id }
                ?: throw IllegalArgumentException("Unknown ConstantPoolTag id: $id")
        }
    }
}
