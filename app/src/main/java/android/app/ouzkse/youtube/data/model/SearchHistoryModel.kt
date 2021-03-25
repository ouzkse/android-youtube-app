package android.app.ouzkse.youtube.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history_table"
)
data class SearchHistoryModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val searchedKeyword: String
)