package android.app.ouzkse.youtube.ui.search

import android.app.ouzkse.youtube.data.model.SearchHistoryModel

interface SearchHistoryItemClickListener {

    fun onClick(historyItem: SearchHistoryModel)
}