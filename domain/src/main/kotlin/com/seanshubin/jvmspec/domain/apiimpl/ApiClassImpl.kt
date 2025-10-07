package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiClassImpl(private val classFile: ClassFile) : ApiClass {
    override fun origin(): String {
        return classFile.origin.id
    }

    override fun className(): String {
        return classFile.constantPoolLookup.className(classFile.thisClass)
    }

    override fun methods(): List<ApiMethod> {
        return classFile.methods.indices.map {
            ApiMethodImpl(classFile, it)
        }
    }

    override fun disassemblyLines(): List<String> {
        return classFile.lines()
    }
}
