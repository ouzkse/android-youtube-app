package android.app.ouzkse.youtube.ui.settings

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }
}