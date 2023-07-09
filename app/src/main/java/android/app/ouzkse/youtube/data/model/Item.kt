package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "item_storage_table",
    primaryKeys = ["video_id"]
)
@Parcelize
data class Item(
    @Embedded val id: Id,
    @Embedded val snippet: Snippet,
    @ColumnInfo(name = "is_watch_later") var isWatchLater: Boolean,
    @ColumnInfo(name = "is_favourite") var isFavourite: Boolean
) : Parcelable