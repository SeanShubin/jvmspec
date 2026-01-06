package com.seanshubin.jvmspec.configuration

import java.nio.file.Path

interface FixedPathJsonFileKeyValueStoreFactory {
    fun create(path: Path): KeyValueStore
}
