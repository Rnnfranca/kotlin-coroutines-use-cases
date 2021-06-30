package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.FakeSuccessApi
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainConcurrentScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially() should load data sequentially`() =
        mainConcurrentScopeRule.runBlockingTest {

            //Arrange
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi3(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            observeViewModel(viewModel)

            // act
            viewModel.performNetworkRequestsSequentially()
            val forwardedTime = advanceUntilIdle()

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ), receivedUiStates
            )
            Assert.assertEquals(3000, forwardedTime)

        }


    @Test
    fun `performNetworkRequestsConcurrently() should load data concurrently`() =
        mainConcurrentScopeRule.runBlockingTest {

            // Arrange
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi3(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequestsConcurrently()
            val forwardedTime = advanceUntilIdle()

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ), receivedUiStates
            )

            Assert.assertEquals(1000, forwardedTime)
        }

    private fun observeViewModel(viewModel: PerformNetworkRequestsConcurrentlyViewModel) {
        viewModel.uiState().observeForever { uiState ->
            uiState?.let {
                receivedUiStates.add(uiState)
            }

        }
    }
}