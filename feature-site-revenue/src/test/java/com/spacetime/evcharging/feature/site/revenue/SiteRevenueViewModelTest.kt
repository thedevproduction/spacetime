package com.spacetime.evcharging.feature.site.revenue

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.Money
import com.spacetime.evcharging.data.model.SettlementRecord
import com.spacetime.evcharging.data.model.SiteAdminRevenue
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SiteRevenueViewModelTest {

    private val siteAdminRepository = mockk<SiteAdminRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SiteRevenueViewModel

    private val mockRevenue = SiteAdminRevenue(
        grossChargingValue = Money(845000),
        platformCommission = Money(84500),
        gatewayFees = Money(16900),
        taxAmount = Money(152100),
        netSettlement = Money(591500),
        paidSettlements = Money(420000),
        pendingSettlements = Money(171500)
    )

    private val mockSettlements = listOf(
        SettlementRecord("SET-01", "Week 1", Money(420000), Money(105000), Money(315000), "PAID", "08 Nov 2026")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { siteAdminRepository.getRevenueSummary() } returns AppResult.Success(mockRevenue)
        coEvery { siteAdminRepository.getSettlements() } returns AppResult.Success(mockSettlements)
        viewModel = SiteRevenueViewModel(siteAdminRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadRevenue sets revenue and settlements in state`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.revenue)
        assertEquals(591500, state.revenue?.netSettlement?.amountMinor)
        assertEquals(1, state.settlements.size)
    }
}
