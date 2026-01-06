package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract

interface Environment {
    val files: FilesContract
}
