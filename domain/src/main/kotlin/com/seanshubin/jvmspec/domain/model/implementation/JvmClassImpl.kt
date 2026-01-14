package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile
import com.seanshubin.jvmspec.domain.model.api.*
import java.nio.file.Path
import java.util.*

class JvmClassImpl(
    private val classFile: ClassFile,
    private val methodFactory: JvmMethodFactory,
    private val fieldFactory: JvmFieldFactory,
    private val attributeFactory: JvmAttributeFactory
) : JvmClass {
    override val constants: SortedMap<UShort, JvmConstant> = classFile.constantPool.associate {
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
        val toJvmMethod = { methodInfo: com.seanshubin.jvmspec.domain.classfile.structure.MethodInfo ->
            methodFactory.createMethod(this, methodInfo)
        }
        return classFile.methods.map(toJvmMethod)
    }

    override fun interfaces(): List<JvmConstant> {
        return classFile.interfaces.map {
            constants.getValue(it)
        }
    }

    override fun fields(): List<JvmField> {
        val toJvmField = { fieldInfo: com.seanshubin.jvmspec.domain.classfile.structure.FieldInfo ->
            fieldFactory.createField(this, fieldInfo)
        }
        return classFile.fields.map(toJvmField)
    }

    override val accessFlags: Set<AccessFlag> = classFile.accessFlags

    override fun attributes(): List<JvmAttribute> {
        val toJvmAttribute = { attributeInfo: com.seanshubin.jvmspec.domain.classfile.structure.AttributeInfo ->
            attributeFactory.createAttribute(this, attributeInfo)
        }
        return classFile.attributes.map(toJvmAttribute)
    }
}
