package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.*
import java.lang.RuntimeException

fun main() {

    val exceptionHandler = CoroutineExceptionHandler {coroutineContext, throwable ->
        println("Caught exception $throwable")
    }

    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)

    scope.launch {
        println("Coroutine 1 starts")
        delay(50)
        println("Coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(500)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 got cancelled!")
        }
    }

    Thread.sleep(1000)
    println("Scope got cancelled: ${!scope.isActive}")
}