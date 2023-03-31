package android.app.ouzkse.youtube.di

import android.app.ouzkse.youtube.BuildConfig
import android.app.ouzkse.youtube.data.api.YouTubeService
import android.app.ouzkse.youtube.data.local.YouTubeDao
import android.app.ouzkse.youtube.data.local.YouTubeRoomDatabase
import android.app.ouzkse.youtube.data.remote.YouTubeRemoteDataSource
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @ApiKey
    @Provides
    @Singleton
    fun provideApiKey() = BuildConfig.API_KEY

    @BaseUrl
    @Provides
    @Singleton
    fun provideBaseUrl() = "https://www.googleapis.com/youtube/v3/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, @BaseUrl baseUrl: String): Retrofit =
        Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideYouTubeService(retrofit: Retrofit): YouTubeService =
        retrofit.create(YouTubeService::class.java)

    @Provides
    @Singleton
    fun provideYouTubeRemoteDataSource(
        youtubeService: YouTubeService,
        @ApiKey apiKey: String
    ): YouTubeRemoteDataSource = YouTubeRemoteDataSource(youtubeService, apiKey)

    @Provides
    @Singleton
    fun provideYouTubeRepository(
        remoteDataSource: YouTubeRemoteDataSource,
        youTubeDao: YouTubeDao,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): YouTubeRepository = YouTubeRepository(remoteDataSource, youTubeDao, dispatcher)

    @Provides
    @Singleton
    fun provideYouTubeRoomDatabase(
        @ApplicationContext context: Context
    ): YouTubeRoomDatabase = YouTubeRoomDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideYouTubeDao(database: YouTubeRoomDatabase): YouTubeDao = database.youtubeDao()

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}