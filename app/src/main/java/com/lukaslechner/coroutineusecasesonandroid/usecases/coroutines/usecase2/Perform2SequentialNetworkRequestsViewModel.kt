package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                val recentAndroidFeatures = mockApi.getAndroidVersionFeatures(recentAndroidVersions.last().apiLevel)
                uiState.value = UiState.Success(recentAndroidFeatures)
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network request failed")
            }

        }
    }
}