package com.seanshubin.jvmspec.domain.samples

object SampleApp2 {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies().sample.run(args)
    }
}
