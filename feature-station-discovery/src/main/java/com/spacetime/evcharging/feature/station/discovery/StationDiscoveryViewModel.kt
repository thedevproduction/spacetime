package com.spacetime.evcharging.feature.station.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.data.model.Station
import com.spacetime.evcharging.data.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class StationSortOption(val displayName: String) {
    NEAREST("Nearest"),
    LOWEST_PRICE("Lowest Price"),
    FASTEST_CHARGER("Fastest"),
    HIGHEST_RATED("Top Rated")
}

enum class MapEngine {
    INTERACTIVE_MAP,
    GOOGLE_MAPS
}

data class StationFilterState(
    val availableNowOnly: Boolean = false,
    val selectedCategory: String = "ALL", // ALL, AC, DC
    val selectedConnectors: Set<String> = emptySet(),
    val maxDistanceKm: Float = 50f,
    val open24Hours: Boolean = false,
    val selectedAmenities: Set<String> = emptySet()
)

data class StationDiscoveryUiState(
    val stations: List<Station> = emptyList(),
    val filteredStations: List<Station> = emptyList(),
    val selectedStation: Station? = null,
    val userLatitude: Double = 23.1815,
    val userLongitude: Double = 79.9864,
    val isMapMode: Boolean = true,
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val sortOption: StationSortOption = StationSortOption.NEAREST,
    val filters: StationFilterState = StationFilterState(),
    val activeQuickFilter: String = "ALL",
    val mapEngine: MapEngine = MapEngine.INTERACTIVE_MAP,
    val isFilterSheetVisible: Boolean = false
)

@HiltViewModel
class StationDiscoveryViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StationDiscoveryUiState())
    val uiState: StateFlow<StationDiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadStations()
    }

    fun loadStations(lat: Double = _uiState.value.userLatitude, lng: Double = _uiState.value.userLongitude) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, userLatitude = lat, userLongitude = lng) }
            when (val res = stationRepository.getNearbyStations(lat, lng)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            stations = res.data,
                            filteredStations = applyFiltersAndSort(res.data, it.searchQuery, it.filters, it.sortOption, it.activeQuickFilter)
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    fun updateUserLocation(lat: Double, lng: Double) {
        loadStations(lat, lng)
    }

    fun toggleViewMode() {
        _uiState.update { it.copy(isMapMode = !it.isMapMode) }
    }

    fun toggleMapEngine() {
        _uiState.update {
            val next = if (it.mapEngine == MapEngine.INTERACTIVE_MAP) MapEngine.GOOGLE_MAPS else MapEngine.INTERACTIVE_MAP
            it.copy(mapEngine = next)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredStations = applyFiltersAndSort(it.stations, query, it.filters, it.sortOption, it.activeQuickFilter)
            )
        }
    }

    fun setQuickFilter(tag: String) {
        _uiState.update { current ->
            val nextTag = if (current.activeQuickFilter == tag) "ALL" else tag
            val updatedFilters = when (nextTag) {
                "AVAILABLE_NOW" -> current.filters.copy(availableNowOnly = true)
                "24_HOURS" -> current.filters.copy(open24Hours = true)
                else -> current.filters.copy(availableNowOnly = false, open24Hours = false)
            }
            val updatedSort = when (nextTag) {
                "CHEAP" -> StationSortOption.LOWEST_PRICE
                "NEAREST" -> StationSortOption.NEAREST
                else -> current.sortOption
            }
            current.copy(
                activeQuickFilter = nextTag,
                filters = updatedFilters,
                sortOption = updatedSort,
                filteredStations = applyFiltersAndSort(current.stations, current.searchQuery, updatedFilters, updatedSort, nextTag)
            )
        }
    }

    fun selectStation(station: Station?) {
        _uiState.update { it.copy(selectedStation = station) }
    }

    fun showFilterSheet(show: Boolean) {
        _uiState.update { it.copy(isFilterSheetVisible = show) }
    }

    fun updateSortOption(option: StationSortOption) {
        _uiState.update {
            it.copy(
                sortOption = option,
                filteredStations = applyFiltersAndSort(it.stations, it.searchQuery, it.filters, option, it.activeQuickFilter)
            )
        }
    }

    fun applyFilters(filters: StationFilterState) {
        _uiState.update {
            it.copy(
                filters = filters,
                isFilterSheetVisible = false,
                filteredStations = applyFiltersAndSort(it.stations, it.searchQuery, filters, it.sortOption, it.activeQuickFilter)
            )
        }
    }

    fun resetFilters() {
        applyFilters(StationFilterState())
    }

    private fun applyFiltersAndSort(
        list: List<Station>,
        query: String,
        filters: StationFilterState,
        sort: StationSortOption,
        quickFilter: String
    ): List<Station> {
        var result = list

        if (query.isNotBlank()) {
            result = result.filter {
                it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
            }
        }

        if (filters.availableNowOnly || quickFilter == "AVAILABLE_NOW") {
            result = result.filter { it.availableConnectors > 0 && it.availability == ChargerStatus.AVAILABLE }
        }

        if (filters.open24Hours || quickFilter == "24_HOURS") {
            result = result.filter { it.operatingHours.contains("24", ignoreCase = true) }
        }

        if (quickFilter == "FAST_DC") {
            result = result.filter { it.name.contains("Superhub", ignoreCase = true) || it.name.contains("Fast", ignoreCase = true) || it.totalConnectors >= 4 }
        }

        result = result.filter { it.distanceKm <= filters.maxDistanceKm }

        return when (sort) {
            StationSortOption.NEAREST -> result.sortedBy { it.distanceKm }
            StationSortOption.LOWEST_PRICE -> result.sortedBy { it.minimumTariff.amountMinor }
            StationSortOption.HIGHEST_RATED -> result.sortedByDescending { it.rating }
            StationSortOption.FASTEST_CHARGER -> result.sortedByDescending { it.totalConnectors }
        }
    }
}
