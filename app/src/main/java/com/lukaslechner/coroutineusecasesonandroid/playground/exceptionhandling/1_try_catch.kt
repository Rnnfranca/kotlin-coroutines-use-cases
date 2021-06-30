package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.RuntimeException

fun main() {

    val scope = CoroutineScope(Job())

    scope.launch {
        try {
            launch {
                functionThatThrows()
            }
        } catch (e: Exception) {
            println("Caught: $e")
        }
    }

    Thread.sleep(100)
}

private fun functionThatThrows() {
    // some code
    throw RuntimeException()
}