package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class High(
    @ColumnInfo(name = "video_thumbnail_url") val url: String
) : Parcelable