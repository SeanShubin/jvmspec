package com.seanshubin.jvmspec.infrastructure.time

import java.time.Clock

class Timer(private val clock: Clock) {
    fun withTimerMilliseconds(f: () -> Unit): Long {
        val startTime = clock.millis()
        f()
        val endTime = clock.millis()
        val durationMillis = endTime - startTime
        return durationMillis
    }
}
