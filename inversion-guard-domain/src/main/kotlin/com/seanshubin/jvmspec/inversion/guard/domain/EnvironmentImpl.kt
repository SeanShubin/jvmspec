package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.di.contract.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment
