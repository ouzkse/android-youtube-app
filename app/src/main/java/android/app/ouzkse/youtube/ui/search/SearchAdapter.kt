package android.app.ouzkse.youtube.ui.search

import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SearchAdapter(private val clickListener: SearchHistoryItemClickListener) :
    ListAdapter<SearchHistoryModel, SearchHistoryViewHolder>
        (SearchHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class SearchHistoryDiffCallback : DiffUtil.ItemCallback<SearchHistoryModel>() {

        override fun areItemsTheSame(
            oldItem: SearchHistoryModel,
            newItem: SearchHistoryModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SearchHistoryModel,
            newItem: SearchHistoryModel
        ) = oldItem == newItem
    }
}