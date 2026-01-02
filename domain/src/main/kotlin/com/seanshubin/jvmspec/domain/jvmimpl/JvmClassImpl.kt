package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.jvm.*
import com.seanshubin.jvmspec.domain.primitive.AccessFlag
import java.nio.file.Path
import java.util.*

class JvmClassImpl(private val classFile: ClassFile) : JvmClass {
    override val constants: SortedMap<UShort, JvmConstant.Constant> = classFile.constantPool.associate {
        it.index to JvmConstantFactory.createByIndex(classFile.constantPoolMap, it.index)
    }.toSortedMap()

    override val origin: Path = classFile.origin
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

    override fun methods(): List<JvmMethod> {
        return classFile.methods.map {
            JvmMethodImpl(this, it)
        }
    }

    override fun interfaces(): List<JvmConstant.Constant> {
        return classFile.interfaces.map {
            constants.getValue(it)
        }
    }

    override fun fields(): List<JvmField> {
        return classFile.fields.map {
            JvmFieldImpl(this, it)
        }
    }

    override val accessFlags: Set<AccessFlag> = classFile.accessFlags

    override fun attributes(): List<JvmAttribute> {
        return classFile.attributes.map {
            JvmAttributeImpl(this, it)
        }
    }
}
