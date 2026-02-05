package com.seanshubin.jvmspec.configuration

import com.seanshubin.jvmspec.di.contract.FilesContract
import java.nio.file.Path

class FixedPathJsonFileKeyValueStoreFactoryImpl(val files: FilesContract) : FixedPathJsonFileKeyValueStoreFactory {
    override fun create(path: Path): KeyValueStore {
        return FixedPathJsonFileKeyValueStore(files, path)
    }
}
