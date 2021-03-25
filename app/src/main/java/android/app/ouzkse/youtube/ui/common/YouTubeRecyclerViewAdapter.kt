package android.app.ouzkse.youtube.ui.common

import android.app.ouzkse.youtube.data.model.Item
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class YouTubeRecyclerViewAdapter(private val clickListener: YouTubeItemOnClickListener) :
    ListAdapter<Item, YouTubeItemViewHolder>(YouTubeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeItemViewHolder {
        return YouTubeItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: YouTubeItemViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class YouTubeItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ) = oldItem == newItem
    }
}