package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag

interface ConstantInfo {
    val tag: ConstantPoolTag
    val index: UShort
    val entriesTaken: Int
}
