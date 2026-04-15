package com.optic.iptv.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.optic.iptv.R
import com.optic.iptv.data.auth.AuthPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

data class LoginState(
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun onCodeDigitEntered(digit: String) {
        if (_uiState.value.code.length < 12) {
            _uiState.value = _uiState.value.copy(
                code = _uiState.value.code + digit,
                error = null
            )
        }
    }

    fun onBackspace() {
        if (_uiState.value.code.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                code = _uiState.value.code.dropLast(1),
                error = null
            )
        }
    }

    fun submitCode() {
        val code = _uiState.value.code
        if (code.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val dbRef = FirebaseDatabase.getInstance().getReference("sync/global/loginCodes")
                val snapshot = dbRef.get().await()

                val normalizedInput = code.trim().lowercase()
                var found = false
                val now = Instant.now()

                snapshot.children.forEach { child ->
                    val item = child.value as? Map<*, *>
                    val active = item?.get("active") != false
                    val dbCode = item?.get("code")?.toString()?.trim()?.lowercase() ?: ""
                    val expiresAt = item?.get("expiresAt")?.toString()

                    if (active && dbCode == normalizedInput) {
                        if (expiresAt == null || expiresAt.equals("permanent", ignoreCase = true)) {
                            found = true
                        } else {
                            try {
                                val expiry = Instant.parse(expiresAt)
                                if (now.isBefore(expiry)) {
                                    found = true
                                }
                            } catch (_: Exception) {
                                found = true
                            }
                        }
                    }
                }

                if (found) {
                    AuthPreferences.setLoggedIn(getApplication(), true)
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = getApplication<Application>().getString(R.string.error_invalid_code)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = getApplication<Application>().getString(
                        R.string.error_connection,
                        e.message.orEmpty()
                    )
                )
            }
        }
    }
}
