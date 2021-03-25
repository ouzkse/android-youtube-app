package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Id(
    @PrimaryKey
    @ColumnInfo(name = "video_id")
    val videoId: String
) : Parcelable