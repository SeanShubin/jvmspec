package com.seanshubin.jvmspec.infrastructure.command

import com.seanshubin.jvmspec.di.contract.FilesContract

interface Environment {
    val files: FilesContract
}