package android.app.ouzkse.youtube.data.local

import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface YouTubeDao {

    @Query("select * from item_storage_table")
    fun getLocalVideoInformation(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Query("delete from item_storage_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyModel: SearchHistoryModel)

    @Query("select * from search_history_table order by id desc")
    fun getSearchHistoryItems(): Flow<List<SearchHistoryModel>>

    @Query("delete from search_history_table")
    suspend fun clearSearchHistory()
}