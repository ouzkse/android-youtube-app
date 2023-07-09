package android.app.ouzkse.youtube.util

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.ui.common.LibraryMenuItemIds
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("image")
fun setImage(imageView: ImageView, item: Item) {
    val url = item.snippet.thumbnails.high.url
    imageView.load(url) {
        allowHardware(false)
        crossfade(true)
    }
    imageView.adjustViewBounds = true
    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
}

@BindingAdapter("drawableTint")
fun ImageView.setDrawableTint(status: Boolean) {
    if (status) {
        setColorFilter(context.themeColor(R.attr.colorPrimary))
    } else {
        setColorFilter(context.themeColor(R.attr.colorControlNormal))
    }
}

@BindingAdapter("menuIcon")
fun ImageView.setIcon(id: Int) {
    val drawable = when (id) {
        LibraryMenuItemIds.FAVOURITE -> R.drawable.ic_favorite_24dp
        else -> R.drawable.ic_watch_later_24dp
    }
    setImageResource(drawable)
    setColorFilter(context.themeColor(R.attr.colorPrimary))
}