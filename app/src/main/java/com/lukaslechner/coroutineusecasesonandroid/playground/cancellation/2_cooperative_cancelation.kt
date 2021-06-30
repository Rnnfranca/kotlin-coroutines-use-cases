package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {

    val job = launch(Dispatchers.Default) {

        nonCancellable()
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()

}

private suspend fun nonCancellable() = coroutineScope{
    repeat(10) { index ->
        if (isActive) {
            println("operation number $index")
            Thread.sleep(100)
        } else {
            // NonCancellable is always active
            withContext(NonCancellable) {
                delay(100)
                println("Cleaning up ...")
                throw CancellationException()
            }
        }
    }
}

private suspend fun cooperativeCancellation1() = coroutineScope {
    repeat(10) { index ->
        if (isActive) {
            println("operation number $index")
            Thread.sleep(100)
        } else {
            println("Cleaning up ...")
            throw CancellationException()
        }
    }
}

private suspend fun cooperativeCancellation2() = coroutineScope {
    repeat(10) { index ->
        ensureActive()
        println("operation number $index")
        Thread.sleep(100)
    }
}

private suspend fun cooperativeCancellation3() = coroutineScope {
    repeat(10) { index ->
        yield()
        println("operation number $index")
        Thread.sleep(100)
    }
}