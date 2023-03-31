package android.app.ouzkse.youtube.ui.search

import android.app.ouzkse.youtube.data.Result
import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    private val _searchHistory = repository
        .getSearchedItems()
        .asLiveData(viewModelScope.coroutineContext)

    val searchHistory: LiveData<List<SearchHistoryModel>>
        get() = _searchHistory

    private val _searchLoadingStatus = MutableLiveData(false)
    val searchLoadingStatus: LiveData<Boolean?>
        get() = _searchLoadingStatus

    private val _items = MutableLiveData<List<Item>?>()
    val items: LiveData<List<Item>?>
        get() = _items

    val listSize: Int
        get() = _items.value?.size ?: 0

    private var nextPageToken: String = ""
    private var lastSearchKeyword: String = ""

    fun saveSearchKeyword(keyword: String) {
        if (_searchHistory.value?.map { it.searchedKeyword }?.contains(keyword) == false) {
            viewModelScope.launch {
                repository.saveSearchKeyword(SearchHistoryModel(0, keyword))
            }
        }
        lastSearchKeyword = keyword
        setLoadingStatus(true)
        fetchData()
    }

    fun fetchData() {
        serviceFetchingJob = viewModelScope.launch {
            repository.searchVideos(lastSearchKeyword, nextPageToken).collect { result ->
                when (result) {
                    is Result.Success -> {
                        saveItems(result.data?.items)
                        saveNextPageToken(result.data?.nextPageToken)
                        setLoadingStatus(null)
                    }
                    is Result.Failure -> setLoadingStatus(true)
                }
            }
        }
    }

    private fun saveItems(items: List<Item>?) {
        items?.let {
            val currentList = if (_items.value == null) {
                arrayListOf()
            } else {
                arrayListOf<Item>().apply {
                    addAll(_items.value as ArrayList)
                }
            }
            currentList.addAll(items)
            _items.value = currentList
        }
    }

    private fun saveNextPageToken(token: String?) {
        token?.let { nextPageToken = it }
    }

    fun setLoadingStatus(status: Boolean?) {
        _searchLoadingStatus.value = status
    }

    fun clearSearchResults() {
        nextPageToken = ""
        _items.value = null
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