package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*
import java.lang.RuntimeException

fun main() {

    // uma exceção de coroutina sempre será propagada para o nivel do job, sendo uma exceção de um
    // launch ou de um async
    // porém se a coroutina do topo for iniciada por .launch a exception será tratada pela CoroutineExceptionHandler
    // ou caso o escopo não tenha uma CoroutineExceptionHandler, a exceção será passata para a thread
    // atual tratar a exceção
    // por outro lado se a coroutina do topo for iniciada com .async a exceção será encapusalada
    // no retorno deferred do async e será lançada quando o .await() for chamado



    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }
    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        async {
            delay(200)
            throw RuntimeException()
        }
    }

    Thread.sleep(500)
}

private fun asyncExceptionHandler(scope: CoroutineScope) {
    val deferred = scope.async {

        delay(200)

        throw RuntimeException()
    }

    scope.launch {
        deferred.await()
    }
}