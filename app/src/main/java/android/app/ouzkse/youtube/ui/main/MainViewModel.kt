package android.app.ouzkse.youtube.ui.main

import android.app.ouzkse.youtube.data.Result
import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    private val _loadingStatus = MutableLiveData<Boolean>(true)
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    private var nextPageToken: String = ""
    var listSize: Int = 0

    init {
        getPopularVideos()
    }

    fun getPopularVideos() {
        serviceFetchingJob = viewModelScope.launch {
            repository.getPopularVideos(nextPageToken).collect { result ->
                when (result) {
                    is Result.Success -> {
                        saveItems(result.data?.items)
                        saveNextPageToken(result.data?.nextPageToken)
                        setServiceStatus(false)
                    }
                    is Result.Failure -> setServiceStatus(true)
                }
            }
        }
    }

    private fun setServiceStatus(status: Boolean) {
        _loadingStatus.value = status
    }

    private fun saveNextPageToken(token: String?) {
        token?.let { nextPageToken = it }
    }

    private fun saveItems(items: List<Item>?) {
        items?.let {
            val currentList = if (_items.value == null) {
                arrayListOf<Item>()
            } else {
                arrayListOf<Item>().apply {
                    addAll(_items.value as ArrayList)
                }
            }
            currentList.addAll(items)
            _items.value = currentList
            listSize = currentList.size
        }
    }

    fun changeWatchLaterStatus(item: Item) {
        _items.value?.firstOrNull { it == item }?.apply {
            this.isWatchLater = this.isWatchLater.not()
        }?.also {
            updateLocalItem(it)
            updateList()
        }
    }

    fun changeFavouriteStatus(item: Item) {
        _items.value?.firstOrNull { it == item }?.apply {
            this.isFavourite = this.isFavourite.not()
        }?.also {
            updateLocalItem(it)
            updateList()
        }
    }

    private fun updateList() {
        _items.value = _items.value
    }

    private fun updateLocalItem(item: Item) {
        viewModelScope.launch {
            repository.insertOrReplaceItem(item)
        }
    }
}