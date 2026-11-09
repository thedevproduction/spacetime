package com.spacetime.evcharging.feature.auth

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.SendOtpResult
import com.spacetime.evcharging.data.repository.AuthRepository
import com.spacetime.evcharging.feature.auth.welcome.WelcomeViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WelcomeViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `entering valid 10-digit Indian phone number marks phone as valid`() {
        viewModel.onPhoneNumberChanged("9876543210")
        val state = viewModel.uiState.value

        assertEquals("9876543210", state.phoneNumber)
        assertTrue(state.isPhoneValid)
        assertNull(state.error)
    }

    @Test
    fun `entering short phone number marks phone as invalid`() {
        viewModel.onPhoneNumberChanged("98765")
        val state = viewModel.uiState.value

        assertEquals("98765", state.phoneNumber)
        assertFalse(state.isPhoneValid)
    }

    @Test
    fun `sendOtp with valid number triggers repository call`() = runTest(testDispatcher) {
        coEvery { authRepository.sendOtp("+91", "9876543210") } returns AppResult.Success(
            SendOtpResult("ch_123", "+91 ******3210", 300, 30)
        )

        viewModel.onPhoneNumberChanged("9876543210")
        viewModel.sendOtp()
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { authRepository.sendOtp("+91", "9876543210") }
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
