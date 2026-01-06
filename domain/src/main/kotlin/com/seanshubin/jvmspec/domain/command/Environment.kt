package com.seanshubin.jvmspec.domain.command

import com.seanshubin.jvmspec.contract.FilesContract

interface Environment {
    val files: FilesContract
}