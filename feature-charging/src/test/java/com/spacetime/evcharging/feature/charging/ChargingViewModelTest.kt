package com.spacetime.evcharging.feature.charging

import androidx.lifecycle.SavedStateHandle
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.Money
import com.spacetime.evcharging.core.common.model.SessionStatus
import com.spacetime.evcharging.data.model.ChargingQuote
import com.spacetime.evcharging.data.model.SessionSummary
import com.spacetime.evcharging.data.repository.ChargingRepository
import com.spacetime.evcharging.data.repository.WalletRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChargingViewModelTest {

    private val chargingRepository = mockk<ChargingRepository>()
    private val walletRepository = mockk<WalletRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ChargingViewModel

    private val mockQuote = ChargingQuote(
        quoteId = "quote_123",
        stationId = "st_01",
        chargerId = "CHG-01-DC",
        connectorId = 1,
        energyRatePerKwh = Money(1400),
        sessionFee = Money(0),
        estimatedTotal = Money(35000),
        walletAvailable = Money(75000)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { chargingRepository.getQuote("quote_123") } returns AppResult.Success(mockQuote)
        viewModel = ChargingViewModel(chargingRepository, walletRepository, SavedStateHandle(mapOf("quoteId" to "quote_123")))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadQuote sets quote in ui state`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.quote)
        assertEquals("quote_123", state.quote?.quoteId)
        assertEquals(35000, state.quote?.estimatedTotal?.amountMinor)
    }

    @Test
    fun `selectPaymentMethod updates selected mode`() {
        viewModel.selectPaymentMethod("UPI")
        assertEquals("UPI", viewModel.uiState.value.selectedPaymentMethod)
    }

    @Test
    fun `startCharging calls repository and changes state to CHARGING`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()
        coEvery { chargingRepository.startSession("quote_123", "WALLET") } returns AppResult.Success("sess_001")
        coEvery { chargingRepository.getActiveSession() } returns AppResult.Success(null)

        var startedSessionId = ""
        viewModel.startCharging { startedSessionId = it }
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { chargingRepository.startSession("quote_123", "WALLET") }
        assertEquals("sess_001", startedSessionId)
        assertEquals(SessionStatus.CHARGING, viewModel.uiState.value.sessionState)
    }
}
