package android.app.ouzkse.youtube.ui.detail

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item>
        get() = _item

    fun setItem(item: Item) {
        _item.value = item
    }

    fun changeFavouriteStatus() {
        _item.value = _item.value?.apply { isFavourite = isFavourite.not() }
        updateLocalItem()
    }

    fun changeWatchLaterStatus() {
        _item.value = _item.value?.apply { isWatchLater = isWatchLater.not() }
        updateLocalItem()
    }

    private fun updateLocalItem() {
        viewModelScope.launch {
            _item.value?.let { repository.insertOrReplaceItem(it) }
        }
    }
}
