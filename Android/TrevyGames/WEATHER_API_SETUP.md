# Weather API Setup Instructions

## Getting Your OpenWeatherMap API Key

1. Go to [OpenWeatherMap](https://openweathermap.org/)
2. Sign up for a free account
3. Navigate to the API section
4. Subscribe to the "5 Day / 3 Hour Forecast" API (free tier)
5. Copy your API key

## Setting Up the API Key

1. Open `app/src/main/java/com/cyberfeedforward/trevygames/repository/WeatherRepository.kt`
2. Replace `YOUR_API_KEY_HERE` with your actual API key:

```kotlin
private val apiKey = "your_actual_api_key_here"
```

## Features Implemented

- **7-day weather forecast** using OpenWeatherMap API
- **Automatic location detection** using GPS
- **Modern Material 3 UI** with Jetpack Compose
- **Error handling** for network and permission issues
- **Pull-to-refresh** functionality
- **Responsive design** with proper loading states

## Permissions Required

The app requests the following permissions:
- `INTERNET` - For API calls
- `ACCESS_FINE_LOCATION` - For precise location
- `ACCESS_COARSE_LOCATION` - For approximate location

## How It Works

1. App launches and checks for location permissions
2. If permissions are granted, automatically gets current location
3. Makes API call to OpenWeatherMap with coordinates
4. Processes and displays 7-day forecast
5. Shows temperature, humidity, wind speed, and weather description

## Testing

1. Build and run the app
2. Grant location permissions when prompted
3. Weather data should load automatically
4. Use the refresh button to reload data

## Troubleshooting

- **No weather data**: Check your API key and internet connection
- **Location permission denied**: Go to app settings and enable location permissions
- **API errors**: Verify your API key is correct and active

