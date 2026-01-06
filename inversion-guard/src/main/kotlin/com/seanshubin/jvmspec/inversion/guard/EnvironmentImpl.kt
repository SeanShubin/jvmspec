package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment
