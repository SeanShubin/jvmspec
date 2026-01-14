package com.seanshubin.jvmspec.domain.infrastructure.command

import com.seanshubin.jvmspec.contract.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment {
}