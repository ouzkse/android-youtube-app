package android.app.ouzkse.youtube.data.remote

import android.app.ouzkse.youtube.data.api.YouTubeService
import android.app.ouzkse.youtube.data.base.BaseRemoteDataSource
import android.app.ouzkse.youtube.di.ApiKey
import javax.inject.Inject

class YouTubeRemoteDataSource @Inject constructor(
    private val youTubeService: YouTubeService,
    @ApiKey private val apiKey: String
) : BaseRemoteDataSource() {

    suspend fun getPopularVideos(token: String = "") = getResult {
        youTubeService.getResult(
            key = apiKey,
            nextPageToken = token
        )
    }

    suspend fun searchVideo(nextPageToken: String = "", searchKeyword: String) = getResult {
        youTubeService.searchData(
            key = apiKey,
            searchKeyword = searchKeyword,
            nextPageToken = nextPageToken
        )
    }
}