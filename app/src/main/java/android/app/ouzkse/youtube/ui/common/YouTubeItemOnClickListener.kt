package android.app.ouzkse.youtube.ui.common

import android.app.ouzkse.youtube.data.model.Item
import android.view.View

interface YouTubeItemOnClickListener {

    fun onItemClick(view: View, item: Item)
    fun onItemOptionMenuClick(view: View, item: Item)
}