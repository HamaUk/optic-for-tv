package com.optic.iptv.ui.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsState(
    val hardwareDecoder: Boolean = true,
    val lowLatencyMode: Boolean = false,
    val analyticsEnabled: Boolean = false,
    val parentalControls: Boolean = false,
    val preferHighestQuality: Boolean = true
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(
        SettingsState(
            hardwareDecoder = prefs.getBoolean(KEY_HW_DECODE, true),
            lowLatencyMode = prefs.getBoolean(KEY_LOW_LATENCY, false),
            analyticsEnabled = prefs.getBoolean(KEY_ANALYTICS, false),
            parentalControls = prefs.getBoolean(KEY_PARENTAL, false),
            preferHighestQuality = prefs.getBoolean(KEY_QUALITY, true)
        )
    )
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun setHardwareDecoder(value: Boolean) {
        prefs.edit().putBoolean(KEY_HW_DECODE, value).apply()
        _state.value = _state.value.copy(hardwareDecoder = value)
    }

    fun setLowLatency(value: Boolean) {
        prefs.edit().putBoolean(KEY_LOW_LATENCY, value).apply()
        _state.value = _state.value.copy(lowLatencyMode = value)
    }

    fun setAnalytics(value: Boolean) {
        prefs.edit().putBoolean(KEY_ANALYTICS, value).apply()
        _state.value = _state.value.copy(analyticsEnabled = value)
    }

    fun setParental(value: Boolean) {
        prefs.edit().putBoolean(KEY_PARENTAL, value).apply()
        _state.value = _state.value.copy(parentalControls = value)
    }

    fun setPreferHighestQuality(value: Boolean) {
        prefs.edit().putBoolean(KEY_QUALITY, value).apply()
        _state.value = _state.value.copy(preferHighestQuality = value)
    }

    companion object {
        private const val PREFS = "optic_settings"
        private const val KEY_HW_DECODE = "hw_decode"
        private const val KEY_LOW_LATENCY = "low_latency"
        private const val KEY_ANALYTICS = "analytics"
        private const val KEY_PARENTAL = "parental"
        private const val KEY_QUALITY = "quality"
    }
}
