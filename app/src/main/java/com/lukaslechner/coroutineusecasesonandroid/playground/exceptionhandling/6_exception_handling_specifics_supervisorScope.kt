package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.RuntimeException

fun main() = runBlocking<Unit> {

    // a scoping function, Supervisor Scop instala um sub escopo independente no topo da hierarquia
    // com um supervisor job. Esse nosso escopo não propaga as exceções de coroutinas inicias
    // no topo da hieraquia. Então esse escopo tem que tratar as exceções por conta própria

    // coroutinas iniciadas diretamente do supervisor scope são top level
    // coroutinas top level se comportam diferente quando são inicias com .launch ou .async.
    // Além disso é possível instalar exceptions handler nelas


    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        try {
            supervisorScope {
                launch {
                    println("CEH: ${coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
            }
        } catch (e: Exception) {
            println("Caught $e")
        }
    }

    Thread.sleep(100)
}