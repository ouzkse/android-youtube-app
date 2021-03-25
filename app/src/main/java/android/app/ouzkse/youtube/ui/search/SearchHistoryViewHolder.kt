package android.app.ouzkse.youtube.ui.search

import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.app.ouzkse.youtube.databinding.SearchHistoryItemLayoutBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryViewHolder(
    private val binding: SearchHistoryItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: SearchHistoryItemClickListener, item: SearchHistoryModel) {
        binding.item = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SearchHistoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SearchHistoryItemLayoutBinding.inflate(layoutInflater, parent, false)
            return SearchHistoryViewHolder(binding)
        }
    }
}