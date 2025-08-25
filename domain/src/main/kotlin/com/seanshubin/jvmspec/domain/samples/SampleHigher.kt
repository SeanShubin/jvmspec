package com.seanshubin.jvmspec.domain.samples

class SampleHigher {
    fun foo() {
        SampleLower().foo()
    }
}