package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

class ApiClassImpl(private val classFile: ClassFile) : ApiClass {
    override val origin: String = classFile.origin.id
    override val magic: Int = classFile.magic.toInt()
    override val minorVersion: Int = classFile.minorVersion.toInt()
    override val majorVersion: Int = classFile.majorVersion.toInt()
    override val thisClassName: String = classFile.constantPoolLookup.className(classFile.thisClass)
    override val superClassName: String =
        if (classFile.superClass.toInt() == 0) {
            when (thisClassName) {
                "module-info", "java/lang/Object" -> "<no super class for $thisClassName>"
                else -> throw RuntimeException("no super class for $thisClassName")
            }
        } else {
            classFile.constantPoolLookup.className(classFile.superClass)
        }

    override fun methods(): List<ApiMethod> {
        return classFile.methods.indices.map {
            ApiMethodImpl(classFile, it)
        }
    }

    override fun disassemblyLines(): List<String> {
        return classFile.lines()
    }

    override val constants: SortedMap<Int, ApiConstant.Constant> = classFile.constantPool.associate {
        it.index to ApiConstantFactory.createByIndex(classFile, it.index)
    }.toSortedMap()

    override fun interfaces(): List<ApiConstant.Constant> {
        return classFile.interfaces.map {
            constants.getValue(it.toInt())
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
