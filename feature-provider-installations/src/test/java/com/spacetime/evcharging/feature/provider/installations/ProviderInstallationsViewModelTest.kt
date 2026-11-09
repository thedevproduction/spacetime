package com.spacetime.evcharging.feature.provider.installations

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.InstallationProject
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProviderInstallationsViewModelTest {

    private val serviceProviderRepository = mockk<ServiceProviderRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProviderInstallationsViewModel

    private val mockInstallations = listOf(
        InstallationProject(
            id = "INST-01",
            customerName = "Test Client",
            siteName = "Test Hub",
            address = "Jabalpur",
            chargerCount = 2,
            scheduledDate = "22 Nov 2026",
            status = "INSTALLATION_IN_PROGRESS"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { serviceProviderRepository.getInstallations() } returns AppResult.Success(mockInstallations)
        viewModel = ProviderInstallationsViewModel(serviceProviderRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInstallations populates installation pipeline`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.installations.size)
        assertEquals("Test Hub", state.installations.first().siteName)
    }

    @Test
    fun `showCreateDialog toggles visibility`() {
        viewModel.showCreateDialog(true)
        assertTrue(viewModel.uiState.value.showCreateDialog)
    }
}
