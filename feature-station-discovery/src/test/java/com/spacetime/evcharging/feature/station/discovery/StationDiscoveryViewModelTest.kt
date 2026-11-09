package com.spacetime.evcharging.feature.station.discovery

import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.core.common.model.Money
import com.spacetime.evcharging.data.model.Station
import com.spacetime.evcharging.data.repository.StationRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StationDiscoveryViewModelTest {

    private val stationRepository = mockk<StationRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: StationDiscoveryViewModel

    private val testStations = listOf(
        Station(
            id = "st_01",
            name = "Highway Hub Jabalpur",
            address = "NH-34, Jabalpur",
            latitude = 23.18,
            longitude = 79.98,
            distanceKm = 2.5,
            isOpen = true,
            availability = ChargerStatus.AVAILABLE,
            availableConnectors = 3,
            totalConnectors = 4,
            minimumTariff = Money(1400),
            rating = 4.8f
        ),
        Station(
            id = "st_02",
            name = "Civic Centre Bhopal",
            address = "MP Nagar, Bhopal",
            latitude = 23.23,
            longitude = 77.43,
            distanceKm = 15.0,
            isOpen = true,
            availability = ChargerStatus.CHARGING,
            availableConnectors = 0,
            totalConnectors = 2,
            minimumTariff = Money(1600),
            rating = 4.2f
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { stationRepository.getNearbyStations(any(), any(), any()) } returns AppResult.Success(testStations)
        viewModel = StationDiscoveryViewModel(stationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search query filters station results correctly`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChanged("Jabalpur")
        val state = viewModel.uiState.value

        assertEquals(1, state.filteredStations.size)
        assertEquals("st_01", state.filteredStations.first().id)
    }

    @Test
    fun `toggleViewMode switches between map and list view`() {
        assertTrue(viewModel.uiState.value.isMapMode)
        viewModel.toggleViewMode()
        assertFalse(viewModel.uiState.value.isMapMode)
    }

    @Test
    fun `quick filter AVAILABLE_NOW filters out stations with zero available connectors`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        viewModel.setQuickFilter("AVAILABLE_NOW")
        val state = viewModel.uiState.value

        assertEquals(1, state.filteredStations.size)
        assertEquals("st_01", state.filteredStations.first().id)
    }

    @Test
    fun `updateUserLocation loads stations with new coordinates`() = runTest(testDispatcher) {
        testScheduler.advanceUntilIdle()

        viewModel.updateUserLocation(23.25, 77.41)
        testScheduler.advanceUntilIdle()

        coVerify { stationRepository.getNearbyStations(23.25, 77.41, any()) }
    }
}
