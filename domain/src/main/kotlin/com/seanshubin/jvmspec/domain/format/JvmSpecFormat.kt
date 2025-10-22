package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiInstruction
import com.seanshubin.jvmspec.domain.tree.Tree

interface JvmSpecFormat {
    fun classTreeList(apiClass: ApiClass): List<Tree>
    fun instructionTree(apiInstruction: ApiInstruction): Tree
}
