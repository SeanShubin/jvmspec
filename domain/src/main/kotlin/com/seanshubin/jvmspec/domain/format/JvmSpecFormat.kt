package com.seanshubin.jvmspec.domain.format

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.tree.Tree

interface JvmSpecFormat {
    fun treeList(apiClass: ApiClass): List<Tree>
}
