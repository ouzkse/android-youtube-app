package android.app.ouzkse.youtube.ui.library

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
data class LibraryMenuItemModel(
    val id: Int,
    val menuTitleText: String
) : Parcelable

interface LibraryMenuItemOnClickListener {

    fun onClick(view: View, item: LibraryMenuItemModel)
}