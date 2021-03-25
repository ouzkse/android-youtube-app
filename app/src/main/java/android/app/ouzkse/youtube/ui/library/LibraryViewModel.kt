package android.app.ouzkse.youtube.ui.library

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import androidx.hilt.lifecycle.ViewModelInject

class LibraryViewModel @ViewModelInject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    fun getMenuItems(titleList: Array<String>) =
        arrayListOf<LibraryMenuItemModel>().apply {
            titleList.forEachIndexed { index, s ->
                add(LibraryMenuItemModel(index, s))
            }
        }
}