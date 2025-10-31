package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.util.*

class ApiClassImpl(private val classFile: ClassFile) : ApiClass {
    override val constants: SortedMap<UShort, ApiConstant.Constant> = classFile.constantPool.associate {
        it.index to ApiConstantFactory.createByIndex(classFile.constantPoolMap, it.index)
    }.toSortedMap()

    override val origin: String = classFile.origin.id
    override val magic: Int = classFile.magic.toInt()
    override val minorVersion: Int = classFile.minorVersion.toInt()
    override val majorVersion: Int = classFile.majorVersion.toInt()
    override val thisClassName: String = lookupClassName(classFile.thisClass)
    override val superClassName: String =
        if (classFile.superClass.toInt() == 0) {
            when (thisClassName) {
                "module-info", "java/lang/Object" -> "<no super class for $thisClassName>"
                else -> throw RuntimeException("no super class for $thisClassName")
            }
        } else {
            lookupClassName(classFile.superClass)
        }

    override fun methods(): List<ApiMethod> {
        return classFile.methods.map {
            ApiMethodImpl(this, it)
        }
    }

    override fun interfaces(): List<ApiConstant.Constant> {
        return classFile.interfaces.map {
            constants.getValue(it)
        }
    }

    override fun fields(): List<ApiField> {
        return classFile.fields.map {
            ApiFieldImpl(this, it)
        }
    }

    override val accessFlags: Set<AccessFlag> = classFile.accessFlags

    override fun attributes(): List<ApiAttribute> {
        return classFile.attributes.map {
            ApiAttributeImpl(this, it)
        }
    }
}
