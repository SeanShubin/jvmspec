package com.seanshubin.jvmspec.domain.output.formatting

import com.seanshubin.jvmspec.domain.infrastructure.collections.Tree
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmInstruction

interface JvmSpecFormat {
    fun classTreeList(jvmClass: JvmClass): List<Tree>
    fun instructionTree(jvmInstruction: JvmInstruction): Tree
}
