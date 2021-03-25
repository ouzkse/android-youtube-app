package android.app.ouzkse.youtube.data.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnails(
    @Embedded val high: High
) : Parcelable