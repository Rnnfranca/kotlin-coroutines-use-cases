package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.TestRule

class PerformSingleNetworkRequestViewModelTest {

    /*  val dispatcher = TestCoroutineDispatcher() // proper dispatcher to be used in test

      @Before
      fun setUp() {
          Dispatchers.setMain(dispatcher) //  this allows us to replace the android main dispatcher
      }

      @After
      fun tearDown() {
          Dispatchers.resetMain() // this clean all of the possible dependencies
          dispatcher.cleanupTestCoroutines() // this function will throw an uncompleted coroutine error
          // if there ar one or more coroutine running, so cleanupTestCoroutines help us to identify
          // leaks that may indicate some problems
      }*/

    // InstantTaskExecutorRule - this rule defines an executer that is used instead of the Android
    // main looper. This rule also configures live data to execute each task synchronously instead
    // of asynchronously
    // in unit tests, asynchronously often happen after our test assertions, so assertions happen
    // to early. To avoid exception or errors in testes, we should make our asynchronous implementations
    // to run synchronously
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return Success when network request is successful`() {

        // arrange - seting up test environment and create instantes of all objects that test need

        val fakeApi = FakeSuccessApi()
        // component to be tested
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)

        observeViewModel(viewModel)

        // act
        viewModel.performSingleNetworkRequest()

        // assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockAndroidVersions)
            ), receivedUiStates
        )
    }

    @Test
    fun `should return Error when network request fails`() {

        // Arrange
        val fakeApi = FakeErrorApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed")
            ),
            receivedUiStates
        )

    }

    private fun observeViewModel(viewModel: PerformSingleNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->
                receivedUiStates.add(uiState)
        }
    }


}