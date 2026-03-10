package com.seanshubin.jvmspec.composition

import com.seanshubin.jvmspec.di.contract.FilesContract
import com.seanshubin.jvmspec.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.model.api.JvmClassFactory
import com.seanshubin.jvmspec.model.api.JvmFieldFactory
import com.seanshubin.jvmspec.model.api.JvmMethodFactory
import com.seanshubin.jvmspec.model.implementation.JvmAttributeFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmClassFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmFieldFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmMethodFactoryImpl
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormat
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.runtime.application.Runner
import java.nio.file.Path
import java.nio.file.Paths

class ApplicationDependencies(
    integrations: Integrations,
    classFilePathString: String
) {
    private val files: FilesContract = integrations.files
    private val emit: (Any?) -> Unit = integrations.emit
    private val classFilePath: Path = Paths.get(classFilePathString)
    private val format: JvmSpecFormat = JvmSpecFormatDetailed()
    private val attributeFactory: JvmAttributeFactory = JvmAttributeFactoryImpl()
    private val methodFactory: JvmMethodFactory = JvmMethodFactoryImpl(attributeFactory)
    private val fieldFactory: JvmFieldFactory = JvmFieldFactoryImpl(attributeFactory)
    private val classFactory: JvmClassFactory = JvmClassFactoryImpl(methodFactory, fieldFactory, attributeFactory)
    val runner: Runnable = Runner(
        files,
        emit,
        classFilePath,
        classFactory,
        format
    )
}
