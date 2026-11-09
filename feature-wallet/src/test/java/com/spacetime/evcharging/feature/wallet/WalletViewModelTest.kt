package com.spacetime.evcharging.feature.wallet

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.Money
import com.spacetime.evcharging.data.model.Wallet
import com.spacetime.evcharging.data.model.WalletTransaction
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
class WalletViewModelTest {

    private val walletRepository = mockk<WalletRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WalletViewModel

    private val mockWallet = Wallet(
        walletId = "wal_01",
        availableBalance = Money(75000),
        reservedBalance = Money(5000),
        promotionalBalance = Money(10000)
    )

    private val mockTransactions = listOf(
        WalletTransaction("tx_01", "Top-up via UPI", Money(50000), "SUCCESS", timestamp = "Today"),
        WalletTransaction("tx_02", "Charging Deduction", Money(-24500), "SUCCESS", timestamp = "Yesterday")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { walletRepository.getWallet() } returns AppResult.Success(mockWallet)
        coEvery { walletRepository.getTransactions() } returns AppResult.Success(mockTransactions)
        viewModel = WalletViewModel(walletRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load populates wallet balance and transactions`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.wallet)
        assertEquals(75000, state.wallet?.availableBalance?.amountMinor)
        assertEquals(2, state.transactions.size)
    }

    @Test
    fun `selectFilter filters transactions by category`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        viewModel.selectFilter("TOPUP")
        val state = viewModel.uiState.value

        assertEquals(1, state.filteredTransactions.size)
        assertTrue(state.filteredTransactions.first().amount.amountMinor > 0)
    }

    @Test
    fun `processTopUp triggers repository call with minor units`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()
        coEvery { walletRepository.createTopUp(50000) } returns AppResult.Success("tx_new")

        viewModel.setTopUpAmount("500")
        var isSuccess = false
        viewModel.processTopUp { isSuccess = true }
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { walletRepository.createTopUp(50000) }
        assertTrue(isSuccess)
    }
}
