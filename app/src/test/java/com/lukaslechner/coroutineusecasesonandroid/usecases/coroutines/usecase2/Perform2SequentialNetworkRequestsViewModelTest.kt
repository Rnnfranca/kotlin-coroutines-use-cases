package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class Perform2SequentialNetworkRequestsViewModelTest {

    @get: Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiState = mutableListOf<UiState>()

    @Test
    fun `should return Success when network request is successful`() {

        // arrange
        val fakeSuccessApi = FakeSuccessApi2()
        val viewmodel = Perform2SequentialNetworkRequestsViewModel(fakeSuccessApi)

        // act
        observeViewModel(viewmodel)

        viewmodel.perform2SequentialNetworkRequest()

        // assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10),
            ), receivedUiState
        )


    }

    @Test
    fun `should return Error when first network request fails`() {
        // arrange
        val fakeErrorApi = FakeErrorApi2()
        val viewmodel = Perform2SequentialNetworkRequestsViewModel(fakeErrorApi)

        // act
        observeViewModel(viewmodel)
        viewmodel.perform2SequentialNetworkRequest()

        // assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed")
            ), receivedUiState
        )

    }

    @Test
    fun `should return Error when second network request fails`() {
        // arrange
        val fakeErrorApi = FakeFeaturesErrorApi()
        val viewmodel = Perform2SequentialNetworkRequestsViewModel(fakeErrorApi)

        // act
        observeViewModel(viewmodel)
        viewmodel.perform2SequentialNetworkRequest()

        // assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed")
            ), receivedUiState
        )

    }

    private fun observeViewModel(viewmodel: Perform2SequentialNetworkRequestsViewModel) {
        viewmodel.uiState().observeForever { uiState ->
            uiState?.let {
                receivedUiState.add(it)
            }
        }
    }
}