package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmInstruction
import com.seanshubin.jvmspec.domain.tree.Tree

interface JvmSpecFormat {
    fun classTreeList(jvmClass: JvmClass): List<Tree>
    fun instructionTree(jvmInstruction: JvmInstruction): Tree
}
