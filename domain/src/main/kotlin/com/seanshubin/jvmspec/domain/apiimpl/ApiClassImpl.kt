package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.*
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
        return if (classFile.superClass.toInt() == 0) {
            when (val thisClassName = thisClassName()) {
                "module-info", "java/lang/Object" -> "<no super-class for $thisClassName>"
                else -> throw RuntimeException("No super class for $thisClassName")
            }
        } else {
            classFile.constantPoolLookup.className(classFile.superClass)
        }
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
            ApiConstantFactory.createByOffset(classFile, it)
        }
    }

    override fun interfaces(): List<ApiConstant.Constant> {
        return classFile.interfaces.map {
            ApiConstantFactory.createByIndex(classFile, it.toInt())
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

    override fun attributes(): List<ApiAttribute> {
        return classFile.attributes.indices.map {
            ApiAttributeImpl(classFile, it)
        }
    }
}
