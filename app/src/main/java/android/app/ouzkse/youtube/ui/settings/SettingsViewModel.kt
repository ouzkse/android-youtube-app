package android.app.ouzkse.youtube.ui.settings

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.hilt.lifecycle.ViewModelInject
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }
}