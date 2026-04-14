package com.optic.iptv.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.optic.iptv.data.model.Category
import com.optic.iptv.data.model.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class DashboardState(
    val categories: List<Category> = emptyList(),
    val channels: List<Channel> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedChannel: Channel? = null,
    val isLoading: Boolean = false
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val db = FirebaseDatabase.getInstance()
                val categorySnap = db.getReference("sync/categories").get().await()
                val categories = categorySnap.children.mapNotNull { it.getValue(Category::class.java)?.copy(id = it.key ?: "") }

                val channelSnap = db.getReference("sync/channels").get().await()
                val channels = channelSnap.children.mapNotNull { it.getValue(Channel::class.java)?.copy(id = it.key ?: "") }

                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    channels = channels,
                    selectedCategory = categories.firstOrNull(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectCategory(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectChannel(channel: Channel) {
        _uiState.value = _uiState.value.copy(selectedChannel = channel)
    }
}
