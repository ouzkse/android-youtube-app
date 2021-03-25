package android.app.ouzkse.youtube.ui.main

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.databinding.FragmentMainBinding
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants.ADD_FAV
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants.ADD_LATER
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants.REMOVE_FAV
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants.REMOVE_LATER
import android.app.ouzkse.youtube.ui.common.YouTubeItemOnClickListener
import android.app.ouzkse.youtube.ui.common.YouTubeRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainFragment : Fragment(), YouTubeItemOnClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: YouTubeRecyclerViewAdapter

    @ExperimentalCoroutinesApi
    private val viewModel: MainViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = YouTubeRecyclerViewAdapter(this)

        binding.recyclerView.adapter = adapter

        setObservers()

        setListeners()
    }

    @ExperimentalCoroutinesApi
    private fun setObservers() {
        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    @ExperimentalCoroutinesApi
    private fun setListeners() {

        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int = layoutManager.findLastVisibleItemPosition()

                if (viewModel.listSize == (lastPosition + 1)) {
                    if (!viewModel.isFetching()) {
                        viewModel.getPopularVideos()
                    }
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    override fun onItemClick(view: View, item: Item) {

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        val cardDetailTransitionName = getString(R.string.video_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
        val directions = MainFragmentDirections.actionMainFragmentToDetailFragment(item)
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
            if (item.isFavourite) this.getItem(REMOVE_FAV).isVisible = true
            else this.getItem(ADD_FAV).isVisible = true

            if (item.isWatchLater) this.getItem(REMOVE_LATER).isVisible = true
            else this.getItem(ADD_LATER).isVisible = true
        }
    }
}