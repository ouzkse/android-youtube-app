package android.app.ouzkse.youtube.ui.common

import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.databinding.YoutubeItemLayoutBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class YouTubeItemViewHolder(
    private val binding: YoutubeItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: YouTubeItemOnClickListener, item: Item) {
        binding.item = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): YouTubeItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = YoutubeItemLayoutBinding.inflate(layoutInflater, parent, false)
            return YouTubeItemViewHolder(binding)
        }
    }
}