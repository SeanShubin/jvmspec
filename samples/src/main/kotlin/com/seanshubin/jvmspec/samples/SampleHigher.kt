package com.seanshubin.jvmspec.samples

class SampleHigher {
    fun foo() {
        SampleLower().foo()
    }
}