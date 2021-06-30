package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

fun main() = runBlocking<Unit> {

    // coroutineScope re-throws as exceptions de seus filhos que falharam, ao invés de propagar
    // para a hieraquia do job, o que permite tratar as exceções


    try {
        doSomething()
    } catch (e: Exception) {
        println("Caught $e ")
    }
}

private suspend fun doSomething() {
    coroutineScope {
        launch {
            throw RuntimeException()
        }
    }
}