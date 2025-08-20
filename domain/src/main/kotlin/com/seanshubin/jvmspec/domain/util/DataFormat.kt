package com.seanshubin.jvmspec.domain.util

object DataFormat {
    fun Int.toDecHex(): String = "${this}(0x${this.toHex()})"
    fun Int.toHex(): String = String.format("%02X", this)
    fun Short.toHex(): String = String.format("%02X", this)
    fun Short.toDecHex(): String = "${this}(0x${this.toHex()})"
    fun UShort.toHex(): String = String.format("%02X", this)
    fun UShort.toDecHex(): String = "${this}(0x${this.toHex()})"
    fun Byte.toDecHex(): String = "${this}(0x${this.toHex()})"
    fun UByte.toHex(): String = this.toByte().toHex()

    fun Byte.toHex(): String = String.format("%02X", this)

    fun List<Byte>.toHex(): String =
        this.toByteArray().toHex()

    fun String.toSanitizedString(): String =
        this.toByteArray().toSanitizedString()

    fun List<Byte>.toSanitizedString(): String =
        this.toByteArray().toSanitizedString()

    fun ByteArray.toHex(): String =
        this.joinToString("") { it.toHex() }

    fun ByteArray.toSanitizedString(): String =
        this.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "." }
}
