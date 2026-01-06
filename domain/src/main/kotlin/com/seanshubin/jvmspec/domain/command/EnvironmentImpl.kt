package com.seanshubin.jvmspec.domain.command

import com.seanshubin.jvmspec.contract.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment {
}