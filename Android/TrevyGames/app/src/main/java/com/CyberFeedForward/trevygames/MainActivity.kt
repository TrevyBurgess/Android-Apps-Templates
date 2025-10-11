package com.cyberfeedforward.trevygames

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyberfeedforward.trevygames.ui.theme.TrevyGamesTheme
import com.cyberfeedforward.trevygames.ui.weather.WeatherScreen
import com.cyberfeedforward.trevygames.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineLocationGranted || coarseLocationGranted) {
            // Permission granted, weather data will be loaded automatically
        } else {
            // Permission denied, show error message
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrevyGamesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherApp(
                        modifier = Modifier.padding(innerPadding),
                        onRequestLocationPermission = {
                            locationPermissionRequest.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherApp(
    modifier: Modifier = Modifier,
    onRequestLocationPermission: () -> Unit
) {
    val context = LocalContext.current
    val weatherViewModel: WeatherViewModel = viewModel()
    val uiState by weatherViewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        weatherViewModel.checkLocationPermission()
    }
    
    LaunchedEffect(uiState.locationPermissionGranted) {
        if (uiState.locationPermissionGranted) {
            weatherViewModel.loadWeatherData()
        }
    }
    
    WeatherScreen(
        uiState = uiState,
        onRefresh = {
            if (uiState.locationPermissionGranted) {
                weatherViewModel.refreshWeatherData()
            } else {
                onRequestLocationPermission()
            }
        },
        modifier = modifier
    )
}