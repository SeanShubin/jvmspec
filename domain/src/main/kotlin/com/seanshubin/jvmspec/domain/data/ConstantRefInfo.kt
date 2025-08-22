package com.seanshubin.jvmspec.domain.data

interface ConstantRefInfo : ConstantInfo {
    val classIndex: UShort
    val nameAndTypeIndex: UShort
}