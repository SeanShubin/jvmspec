package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.di.contract.FilesContract

interface Environment {
    val files: FilesContract
}
