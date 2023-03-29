package android.app.ouzkse.youtube.ui.library

import android.app.ouzkse.youtube.data.base.BaseViewModel
import android.app.ouzkse.youtube.data.repository.YouTubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: YouTubeRepository
) : BaseViewModel() {

    fun getMenuItems(titleList: Array<String>) =
        arrayListOf<LibraryMenuItemModel>().apply {
            titleList.forEachIndexed { index, s ->
                add(LibraryMenuItemModel(index, s))
            }
        }
}