package android.app.ouzkse.youtube.data.repository

import android.app.ouzkse.youtube.data.local.YouTubeDao
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.app.ouzkse.youtube.data.remote.YouTubeRemoteDataSource
import android.app.ouzkse.youtube.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YouTubeRepository @Inject constructor(
    private val remoteDataSource: YouTubeRemoteDataSource,
    private val localDataSource: YouTubeDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun getPopularVideos(nextPageToken: String = "") = flow {
        emit(remoteDataSource.getPopularVideos(nextPageToken))
    }.flowOn(dispatcher)

    fun searchVideos(searchKeyword: String, nextPageToken: String = "") = flow {
        emit(remoteDataSource.searchVideo(nextPageToken, searchKeyword))
    }.flowOn(dispatcher)

    fun getLocalVideoInformation() = localDataSource
        .getLocalVideoInformation()
        .flowOn(dispatcher)

    fun getSearchedItems() = localDataSource
        .getSearchHistoryItems()
        .flowOn(dispatcher)

    suspend fun saveSearchKeyword(searchModel: SearchHistoryModel) =
        localDataSource.insert(searchModel)

    suspend fun clearSearchHistory() = withContext(dispatcher) {
        localDataSource.clearSearchHistory()
    }

    suspend fun insertOrReplaceItem(item: Item) = withContext(dispatcher) {
        localDataSource.insert(item)
    }
}