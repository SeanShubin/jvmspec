package com.seanshubin.jvmspec.classfile.structure

interface ConstantRefInfo : ConstantInfo {
    val classIndex: UShort
    val nameAndTypeIndex: UShort
}