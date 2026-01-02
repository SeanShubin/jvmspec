package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.files.FilesContract

interface Environment {
    val files: FilesContract
}
