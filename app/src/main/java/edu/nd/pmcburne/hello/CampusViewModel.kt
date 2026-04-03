package edu.nd.pmcburne.hello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import edu.nd.pmcburne.hello.data.CampusRepository
import edu.nd.pmcburne.hello.data.CampusLocation

class CampusMapViewModel(
    private val repository: CampusRepository,
) : ViewModel() {

    private val _selectedTag = MutableStateFlow("core")
    val selectedTag: StateFlow<String> = _selectedTag

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val tags: StateFlow<List<String>> = repository
        .getAllTags()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val locations: StateFlow<List<CampusLocation>> = selectedTag
        .flatMapLatest { tag -> repository.getLocationsForTag(tag) }
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    init {
        viewModelScope.launch {
            runCatching { repository.syncFromApi() }
                .onFailure { throwable ->
                    _errorMessage.value = throwable.message ?: "Failed to sync placemarks."
                }
        }
    }

    fun onTagSelected(tag: String) {
        _selectedTag.value = tag
    }

    companion object {
        fun factory(repository: CampusRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CampusMapViewModel(repository) as T
                }
            }
    }
}