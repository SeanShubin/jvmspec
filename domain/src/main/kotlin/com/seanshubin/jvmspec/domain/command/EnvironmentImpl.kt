package com.seanshubin.jvmspec.domain.command

import com.seanshubin.jvmspec.domain.files.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment {
}