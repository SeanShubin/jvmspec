package com.seanshubin.jvmspec.domain.classfile.structure

interface ConstantRefInfo : ConstantInfo {
    val classIndex: UShort
    val nameAndTypeIndex: UShort
}