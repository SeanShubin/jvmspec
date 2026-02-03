package com.seanshubin.jvmspec.runtime.application

import com.seanshubin.jvmspec.contract.FilesContract
import java.time.Clock

interface Integrations {
    val files: FilesContract
    val clock: Clock
    val emit: (Any?) -> Unit
}
