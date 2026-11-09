package com.spacetime.evcharging.feature.vehicles

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.Vehicle
import com.spacetime.evcharging.data.repository.VehicleRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehiclesViewModelTest {

    private val vehicleRepository = mockk<VehicleRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: VehiclesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { vehicleRepository.getVehicles() } returns AppResult.Success(
            listOf(
                Vehicle(
                    id = "veh_01",
                    nickname = "Test EV",
                    category = "FOUR_WHERLER",
                    manufacturer = "Tata Motors",
                    model = "Nexon EV",
                    batteryCapacityKwh = 40.5,
                    connectorTypes = listOf("CCS2"),
                    isDefault = true
                )
            )
        )
        viewModel = VehiclesViewModel(vehicleRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load fetches registered vehicles`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.vehicles.size)
        assertEquals("Test EV", state.vehicles.first().nickname)
    }

    @Test
    fun `toggleConnector updates selected connectors set`() {
        viewModel.toggleConnector("Type 2")
        val state = viewModel.uiState.value

        assertTrue(state.selectedConnectors.contains("Type 2"))
    }
}
