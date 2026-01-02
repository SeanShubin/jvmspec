package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.files.FilesContract

class EnvironmentImpl(
    override val files: FilesContract
) : Environment
