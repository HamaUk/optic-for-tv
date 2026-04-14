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

                val categorySnap = db.getReference("sync/global/channelGroups").get().await()
                val baseCategories = categorySnap.children.mapNotNull { snap ->
                    val name = snap.child("name").getValue(String::class.java) ?: return@mapNotNull null
                    Category(id = snap.key ?: "", name = name, count = 0)
                }

                val channelSnap = db.getReference("sync/global/managedPlaylist").get().await()
                val channels = channelSnap.children.mapNotNull { snap ->
                    val name = snap.child("name").getValue(String::class.java) ?: "Unknown"
                    val url = snap.child("url").getValue(String::class.java) ?: ""
                    val group = snap.child("group").getValue(String::class.java)
                        ?: snap.child("category").getValue(String::class.java) ?: "General"
                    val logo = snap.child("logo").getValue(String::class.java)
                        ?: snap.child("icon_url").getValue(String::class.java) ?: ""

                    val matchingCategory = baseCategories.find { it.name.equals(group, ignoreCase = true) }
                    val categoryId = matchingCategory?.id ?: group

                    Channel(
                        id = snap.key ?: "",
                        name = name,
                        url = url,
                        logo = logo,
                        categoryId = categoryId
                    )
                }

                val dynamicCategoryIds = channels.map { it.categoryId }.distinct()
                val finalCategories = dynamicCategoryIds.map { catId ->
                    val matchedBase = baseCategories.find { it.id == catId }
                    if (matchedBase != null) {
                        matchedBase.copy(count = channels.count { it.categoryId == catId })
                    } else {
                        Category(id = catId, name = catId, count = channels.count { it.categoryId == catId })
                    }
                }

                _uiState.value = _uiState.value.copy(
                    categories = finalCategories,
                    channels = channels,
                    selectedCategory = finalCategories.firstOrNull(),
                    isLoading = false
                )
            } catch (_: Exception) {
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

    fun channelById(id: String): Channel? = _uiState.value.channels.find { it.id == id }
}
