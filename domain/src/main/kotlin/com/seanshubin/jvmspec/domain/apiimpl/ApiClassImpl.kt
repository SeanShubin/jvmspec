package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiConstant
import com.seanshubin.jvmspec.domain.api.ApiField
import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.primitive.AccessFlag

class ApiClassImpl(private val classFile: ClassFile) : ApiClass {
    val constantPoolIndexToOffsetMap = classFile.constantPool.mapIndexed { offset, info ->
        info.index to offset
    }.toMap()
    override fun origin(): String {
        return classFile.origin.id
    }

    override fun magic(): Int {
        return classFile.magic.toInt()
    }

    override fun minorVersion(): Int {
        return classFile.minorVersion.toInt()
    }

    override fun majorVersion(): Int {
        return classFile.majorVersion.toInt()
    }

    override fun thisClassName(): String {
        return classFile.constantPoolLookup.className(classFile.thisClass)
    }

    override fun superClassName(): String {
        return classFile.constantPoolLookup.className(classFile.superClass)
    }

    override fun methods(): List<ApiMethod> {
        return classFile.methods.indices.map {
            ApiMethodImpl(classFile, it)
        }
    }

    override fun disassemblyLines(): List<String> {
        return classFile.lines()
    }

    override fun constants(): List<ApiConstant.Constant> {
        return classFile.constantPool.indices.map {
            ApiConstantFactory.create(classFile, it)
        }
    }

    override fun interfaces(): List<ApiConstant.Constant> {
        return classFile.interfaces.map {
            val offset = constantPoolIndexToOffsetMap.getValue(it.toInt())
            ApiConstantFactory.create(classFile, offset)
        }
    }

    override fun fields(): List<ApiField> {
        return classFile.fields.indices.map {
            ApiFieldImpl(classFile, it)
        }
    }

    override fun accessFlags(): Set<AccessFlag> {
        return classFile.accessFlags
    }
}
