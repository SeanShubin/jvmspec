package com.seanshubin.jvmspec.domain.command

import com.seanshubin.jvmspec.domain.files.FilesContract

interface Environment {
    val files: FilesContract
}