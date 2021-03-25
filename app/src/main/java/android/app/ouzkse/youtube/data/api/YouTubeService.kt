package android.app.ouzkse.youtube.data.api

import android.app.ouzkse.youtube.data.model.YouTubeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeService {

    @GET("search?part=snippet")
    suspend fun getResult(
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 6,
        @Query("regionCode") code: String = "tr",
        @Query("type") type: String = "video",
        @Query("key") key: String,
        @Query("pageToken") nextPageToken: String
    ): Response<YouTubeModel?>

    @GET("search?part=snippet")
    suspend fun searchData(
        @Query("maxResults") maxResults: Int = 3,
        @Query("q") searchKeyword: String,
        @Query("regionCode") code: String = "gb",
        @Query("type") type: String = "video",
        @Query("key") key: String,
        @Query("pageToken") nextPageToken: String
    ): Response<YouTubeModel?>
}