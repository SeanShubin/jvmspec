package com.seanshubin.jvmspec.output.formatting

import com.seanshubin.jvmspec.infrastructure.collections.Tree
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmInstruction

interface JvmSpecFormat {
    fun classTreeList(jvmClass: JvmClass): List<Tree>
    fun instructionTree(jvmInstruction: JvmInstruction): Tree
}
