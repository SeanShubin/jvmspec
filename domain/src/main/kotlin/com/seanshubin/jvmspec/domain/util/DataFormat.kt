package com.seanshubin.jvmspec.domain.util

object DataFormat {
    fun UByte.asHex(): String = this.toByte().asHex()

    fun Byte.asHex(): String = String.format("%02X", this)

    fun List<Byte>.asHex(): String =
        this.toByteArray().asHex()

    fun String.asSanitizedString(): String =
        this.toByteArray().asSanitizedString()

    fun List<Byte>.asSanitizedString(): String =
        this.toByteArray().asSanitizedString()

    fun ByteArray.asHex(): String =
        this.joinToString("") { it.asHex() }

    fun ByteArray.asSanitizedString(): String =
        this.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "." }
}
