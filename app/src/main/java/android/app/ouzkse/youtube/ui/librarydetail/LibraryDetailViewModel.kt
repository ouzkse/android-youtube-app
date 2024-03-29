package android.app.ouzkse.youtube.ui.librarydetail

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import android.app.ouzkse.youtube.ui.common.LibraryMenuItemIds
import android.app.ouzkse.youtube.ui.library.LibraryMenuItemModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryDetailViewModel @Inject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    private val _item = MutableLiveData<LibraryMenuItemModel>()
    val item: LiveData<LibraryMenuItemModel>
        get() = _item

    private val _savedVideoInformation = repository
        .getLocalVideoInformation()
        .asLiveData(viewModelScope.coroutineContext)
    val savedVideoInfo: LiveData<List<Item>>
        get() = _savedVideoInformation.map { list ->
            if (_item.value?.id == LibraryMenuItemIds.FAVOURITE) {
                list.filter { it.isFavourite }.sortedBy { it.snippet.title }
            } else {
                list.filter { it.isWatchLater }.sortedBy { it.snippet.title }
            }
        }

    fun setItem(itemModel: LibraryMenuItemModel) {
        _item.value = itemModel
    }

    fun changeWatchLaterStatus(item: Item) {
        _savedVideoInformation.value?.firstOrNull { it == item }?.apply {
            this.isWatchLater = this.isWatchLater.not()
        }?.also {
            updateLocalItem(it)
        }
    }

    fun changeFavouriteStatus(item: Item) {
        _savedVideoInformation.value?.firstOrNull { it == item }?.apply {
            this.isFavourite = this.isFavourite.not()
        }?.also {
            updateLocalItem(it)
        }
    }

    private fun updateLocalItem(item: Item) {
        viewModelScope.launch {
            repository.insertOrReplaceItem(item)
        }
    }
}