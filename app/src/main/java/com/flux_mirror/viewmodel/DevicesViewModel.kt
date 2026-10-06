package com.flux_mirror.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.flux_mirror.network.DeviceDiscoveryManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// This is the data class your UI will use
data class Device(
    val name: String,
    val location: String,
    val connected: Boolean,
    val type: String,
    val id: String,
    val routeId: String? = null,
    val deviceAddress: String? = null,
    val isWifiDisplay: Boolean = false
)

class DevicesViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "DevicesViewModel"
    }

    private val deviceDiscoveryManager = DeviceDiscoveryManager(application)

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices

    private val discoveredDevicesMap = mutableMapOf<String, Device>()

    // Keeps track of the currently running discovery operation.
    private var discoveryJob: Job? = null

    // This flow tells the Activity to start the permission request
    private val _startMiracastFlow = MutableSharedFlow<Device>()
    val startMiracastFlow = _startMiracastFlow.asSharedFlow()

    // Store selected device for later use
    var selectedDevice: Device? = null

    fun startDiscovery() {
        Log.d(TAG, "Starting discovery...")

        // Cancel any previous discovery collector before starting
        // a new scan. This prevents multiple collectors from running
        // at the same time and returning stale device results.
        discoveryJob?.cancel()

        discoveredDevicesMap.clear()
        _devices.value = emptyList()

        discoveryJob = viewModelScope.launch {
            Log.d(TAG, "Starting discovery in viewModelScope")

            deviceDiscoveryManager.discoverDevices()
                .catch { e ->
                    Log.e(TAG, "Discovery flow error", e)
                }
                .collect { discoveredDevice ->

                    Log.d(
                        TAG,
                        "Device discovered: ${discoveredDevice.name}, " +
                                "type=${discoveredDevice.type}, " +
                                "isWifiDisplay=${discoveredDevice.isWifiDisplay}"
                    )

                    // Check if this is a Miracast device
                    val isMiracast =
                        discoveredDevice.isWifiDisplay &&
                                discoveredDevice.deviceAddress != null

                    // Check if this is an NSD castable device
                    val isNsd =
                        discoveredDevice.type.contains(
                            "Chromecast",
                            ignoreCase = true
                        ) ||
                                discoveredDevice.type.contains(
                                    "Apple TV",
                                    ignoreCase = true
                                ) ||
                                discoveredDevice.type.contains(
                                    "AirPlay",
                                    ignoreCase = true
                                ) ||
                                discoveredDevice.type.contains(
                                    "WiFi Display",
                                    ignoreCase = true
                                )

                    if (!isMiracast && !isNsd) {
                        Log.d(
                            TAG,
                            "Skipping non-castable device: " +
                                    "${discoveredDevice.name} " +
                                    "(Type: ${discoveredDevice.type})"
                        )
                        return@collect
                    }

                    // Create a unique ID and location
                    val deviceId: String
                    val location: String

                    if (isMiracast) {
                        // Miracast device - use MAC address as ID
                        deviceId = discoveredDevice.deviceAddress!!
                        location = "Wireless Display"
                    } else {
                        // NSD device - use IP address or device name
                        deviceId =
                            discoveredDevice.host?.hostAddress
                                ?: discoveredDevice.name

                        location = deviceId
                    }

                    // Create the UI-specific Device object
                    val device = Device(
                        name = discoveredDevice.name,
                        location = location,
                        connected = false,
                        type = discoveredDevice.type,
                        id = deviceId,
                        routeId = if (isMiracast) {
                            discoveredDevice.deviceAddress
                        } else {
                            null
                        },
                        deviceAddress = discoveredDevice.deviceAddress,
                        isWifiDisplay = isMiracast
                    )

                    // Add the device only if it has not already
                    // been discovered during this scan.
                    if (!discoveredDevicesMap.containsKey(device.id)) {
                        discoveredDevicesMap[device.id] = device

                        _devices.value =
                            discoveredDevicesMap.values.toList()

                        Log.d(
                            TAG,
                            "✅ Added device: ${device.name} " +
                                    "(${if (device.isWifiDisplay) {
                                        "Miracast"
                                    } else {
                                        "NSD"
                                    }})"
                        )
                    }
                }
        }
    }

    /**
     * Stops the currently running device discovery.
     */
    fun stopDiscovery() {
        Log.d(TAG, "Stopping discovery...")

        discoveryJob?.cancel()
        discoveryJob = null

        discoveredDevicesMap.clear()
        _devices.value = emptyList()
    }

    /**
     * Make sure discovery is cancelled when the ViewModel
     * is permanently destroyed.
     */
    override fun onCleared() {
        discoveryJob?.cancel()
        discoveryJob = null

        super.onCleared()
    }

    // This function is called from your UI
    fun onDeviceClicked(device: Device) {

        if (device.isWifiDisplay && device.deviceAddress != null) {

            // This is a Miracast device
            Log.d(
                TAG,
                "Miracast device clicked: " +
                        "${device.name} (${device.deviceAddress})"
            )

            selectedDevice = device

            viewModelScope.launch {
                // Tell the Activity to start the permission flow
                _startMiracastFlow.emit(device)
            }

        } else {

            // This is a Chromecast or other NSD device
            Log.d(
                TAG,
                "Clicked on NSD device: ${device.name}"
            )

            selectedDevice = device

            // Chromecast/other NSD connection logic can
            // be implemented here later.
        }
    }

    fun clearSelection() {
        selectedDevice = null
    }
}
