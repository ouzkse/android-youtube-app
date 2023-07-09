package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Id(
    @ColumnInfo(name = "video_id")
    val videoId: String
) : Parcelable