package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag

interface ConstantInfo {
    val tag: ConstantPoolTag
    val index: UShort
    val entriesTaken: Int
}
