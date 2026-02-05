package com.seanshubin.jvmspec.infrastructure.command

import com.seanshubin.jvmspec.di.contract.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment {
}