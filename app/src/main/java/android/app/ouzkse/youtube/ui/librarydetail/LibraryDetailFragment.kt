package android.app.ouzkse.youtube.ui.librarydetail

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.databinding.FragmentLibraryDetailBinding
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants
import android.app.ouzkse.youtube.ui.common.YouTubeItemOnClickListener
import android.app.ouzkse.youtube.ui.common.YouTubeRecyclerViewAdapter
import android.app.ouzkse.youtube.util.ItemOffsetDecoration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class LibraryDetailFragment : Fragment(), YouTubeItemOnClickListener {

    private lateinit var binding: FragmentLibraryDetailBinding
    private lateinit var adapter: YouTubeRecyclerViewAdapter
    private val viewModel: LibraryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = buildContainerTransform()

        sharedElementReturnTransition = buildContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val argument = LibraryDetailFragmentArgs.fromBundle(requireArguments()).libraryItemArg

        viewModel.setItem(argument)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        val layoutManager = GridLayoutManager(context, 2)

        binding.recyclerView.layoutManager = layoutManager

        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.margin_2)

        binding.recyclerView.addItemDecoration(itemDecoration)

        adapter = YouTubeRecyclerViewAdapter(this)

        binding.recyclerView.adapter = adapter

        viewModel.savedVideoInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            interpolator = FastOutSlowInInterpolator()
            scrimColor = Color.TRANSPARENT
            exitTransition = Hold()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
            duration = 300
        }

    override fun onItemClick(view: View, item: Item) {

        val cardDetailTransitionName = getString(R.string.video_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
        val directions = LibraryDetailFragmentDirections
            .actionLibraryDetailFragmentToDetailFragment3(item)
        this.findNavController().navigate(directions, extras)
    }

    @ExperimentalCoroutinesApi
    override fun onItemOptionMenuClick(view: View, item: Item) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_video_item, popupMenu.menu)

        setVideoPopupMenuItems(popupMenu.menu, item)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_watch_later, R.id.remove_watch_later -> {
                    viewModel.changeWatchLaterStatus(item)
                }
                R.id.add_favourite, R.id.remove_favourite -> {
                    viewModel.changeFavouriteStatus(item)
                }
            }
            Snackbar.make(binding.root, menuItem.title, Snackbar.LENGTH_SHORT).show()
            true
        }
        popupMenu.show()
    }

    @ExperimentalCoroutinesApi
    fun setVideoPopupMenuItems(menu: Menu, item: Item) {
        menu.apply {
            if (item.isFavourite) this.getItem(ItemMenuConstants.REMOVE_FAV).isVisible = true
            else this.getItem(ItemMenuConstants.ADD_FAV).isVisible = true

            if (item.isWatchLater) this.getItem(ItemMenuConstants.REMOVE_LATER).isVisible = true
            else this.getItem(ItemMenuConstants.ADD_LATER).isVisible = true
        }
    }
}
