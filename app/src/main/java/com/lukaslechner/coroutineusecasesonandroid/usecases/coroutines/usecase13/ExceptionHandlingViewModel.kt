package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    // o tratamento com o try catch é realmente necessário se quisermos recuperar uma tarefa de uma
    // exceção, por exemplo ,tentando refazer uma requisição à internet.
    // Se não for necessário se recuperar de um exceção então pode ser usado uma CoroutineExceptionHandler
    // que será invocada depois que uma coroutina já ter falhado e neste caso não será possível se
    // recuperar de uma exceção

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(27)
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 500) {
                        // error message 1
                    } else {
                        // error message 2
                    }
                    uiState.value = UiState.Error("Network Request failed: $e")
                }
            }


        }
    }

    fun handleWithCoroutineExceptionHandler() {
        uiState.value = UiState.Loading

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            uiState.value = UiState.Error("Network Request failed!")
        }

        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(27)
        }

    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading

        viewModelScope.launch {

            supervisorScope {

                val oreoFeaturesDeferred = async {
                    api.getAndroidVersionFeatures(27)
                }

                val pieFeaturesDeferred = async {
                    api.getAndroidVersionFeatures(28)
                }

                val android10FeaturesDeferred = async {
                    api.getAndroidVersionFeatures(29)
                }

                val versionFeatures = listOf(
                    oreoFeaturesDeferred,
                    pieFeaturesDeferred,
                    android10FeaturesDeferred
                ).mapNotNull {
                    try {
                        it.await()
                    } catch (e: Exception) {
                        if(e is CancellationException) {
                            throw e
                        }

                        Timber.e("Error loading feature data!")
                        null
                    }
                }

                uiState.value = UiState.Success(versionFeatures)
            }
        }

    }

}