package com.seanshubin.jvmspec.infrastructure.command

import com.seanshubin.jvmspec.contract.FilesContract

interface Environment {
    val files: FilesContract
}