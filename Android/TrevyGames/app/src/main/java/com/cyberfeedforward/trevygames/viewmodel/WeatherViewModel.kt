package com.cyberfeedforward.trevygames.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfeedforward.trevygames.data.WeatherDay
import com.cyberfeedforward.trevygames.location.LocationService
import com.cyberfeedforward.trevygames.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherDays: List<WeatherDay> = emptyList(),
    val error: String? = null,
    val locationPermissionGranted: Boolean = false
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository()
    private val locationService = LocationService(application)
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    fun checkLocationPermission(): Boolean {
        val context = getApplication<Application>()
        val hasFineLocation = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val hasCoarseLocation = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val hasPermission = hasFineLocation || hasCoarseLocation
        _uiState.value = _uiState.value.copy(locationPermissionGranted = hasPermission)
        return hasPermission
    }
    
    fun loadWeatherData() {
        if (!checkLocationPermission()) {
            _uiState.value = _uiState.value.copy(
                error = "Location permission required to get weather data"
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                if (location != null) {
                    val result = weatherRepository.getWeatherForecast(
                        location.latitude,
                        location.longitude
                    )
                    
                    result.fold(
                        onSuccess = { weatherDays ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                weatherDays = weatherDays,
                                error = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to load weather data"
                            )
                        }
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Unable to get current location"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }
    
    fun refreshWeatherData() {
        loadWeatherData()
    }
}

