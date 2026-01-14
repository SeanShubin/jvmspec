package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag

interface ConstantInfo {
    val tag: ConstantPoolTag
    val index: UShort
    val entriesTaken: Int
}
