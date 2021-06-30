package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.RuntimeException

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        print("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        throw RuntimeException()
    }

    Thread.sleep(100)
}