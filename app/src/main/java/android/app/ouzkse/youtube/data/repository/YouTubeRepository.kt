package android.app.ouzkse.youtube.data.repository

import android.app.ouzkse.youtube.data.local.YouTubeDao
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.app.ouzkse.youtube.data.remote.YouTubeRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YouTubeRepository @Inject constructor(
    private val remoteDataSource: YouTubeRemoteDataSource,
    private val localDataSource: YouTubeDao
) {

    @ExperimentalCoroutinesApi
    fun getPopularVideos(nextPageToken: String = "") = flow {
        emit(remoteDataSource.getPopularVideos(nextPageToken))
    }.flowOn(Dispatchers.Default)

    fun searchVideos(searchKeyword: String, nextPageToken: String = "") = flow {
        emit(remoteDataSource.searchVideo(nextPageToken, searchKeyword))
    }.flowOn(Dispatchers.Default)

    fun getLocalVideoInformation() = localDataSource
        .getLocalVideoInformation()
        .flowOn(Dispatchers.Default)

    fun getSearchedItems() = localDataSource
        .getSearchHistoryItems()
        .flowOn(Dispatchers.Default)

    suspend fun saveSearchKeyword(searchModel: SearchHistoryModel) =
        localDataSource.insert(searchModel)

    suspend fun clearSearchHistory() = withContext(Dispatchers.Default) {
        localDataSource.clearSearchHistory()
    }

    suspend fun insertOrReplaceItem(item: Item) = withContext(Dispatchers.Default) {
        localDataSource.insert(item)
    }

}