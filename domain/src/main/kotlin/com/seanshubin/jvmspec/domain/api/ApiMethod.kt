package com.seanshubin.jvmspec.domain.api

interface ApiMethod : ApiFieldOrMethod {
    fun code(): ApiCodeAttribute?
}
