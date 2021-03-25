package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Snippet(
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "channel_title") val channelTitle: String,
    @ColumnInfo(name = "video_description") val description: String,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    @Embedded val thumbnails: Thumbnails,
    @ColumnInfo(name = "video_title") val title: String
) : Parcelable