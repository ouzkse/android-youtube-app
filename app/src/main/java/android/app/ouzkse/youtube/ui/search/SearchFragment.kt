package android.app.ouzkse.youtube.ui.search

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.app.ouzkse.youtube.databinding.FragmentSearchBinding
import android.app.ouzkse.youtube.ui.MainActivity
import android.app.ouzkse.youtube.ui.common.ItemMenuConstants
import android.app.ouzkse.youtube.ui.common.YouTubeItemOnClickListener
import android.app.ouzkse.youtube.ui.common.YouTubeRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.activity.addCallback
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
class SearchFragment : Fragment(), SearchHistoryItemClickListener, YouTubeItemOnClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchHistoryAdapter: SearchAdapter
    private lateinit var searchResultAdapter: YouTubeRecyclerViewAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (viewModel.searchLoadingStatus.value == false) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                viewModel.setLoadingStatus(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        setAdapters()

        setSearchListener()

        setObservers()
    }

    private fun setAdapters() {

        searchResultAdapter = YouTubeRecyclerViewAdapter(this)

        searchHistoryAdapter = SearchAdapter(this)

        if (viewModel.searchLoadingStatus.value == false) {
            binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
        } else {
            binding.searchHistoryRecyclerView.adapter = searchResultAdapter
        }

        val layoutManager = binding.searchHistoryRecyclerView.layoutManager as LinearLayoutManager

        binding.searchHistoryRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (viewModel.searchLoadingStatus.value == null) {
                    val lastPosition: Int = layoutManager.findLastVisibleItemPosition()

                    if (viewModel.listSize == (lastPosition + 1)) {
                        if (!viewModel.isFetching()) {
                            viewModel.fetchData()
                        }
                    }
                }
            }
        })
    }

    private fun setSearchListener() {

        binding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    (requireActivity() as MainActivity).hideKeyboard(view)
                    viewModel.clearSearchResults()
                    viewModel.saveSearchKeyword(view.text.toString())
                    binding.searchHistoryRecyclerView.adapter = searchResultAdapter
                    false
                }
                else -> true
            }
        }

        binding.searchField.setStartIconOnClickListener {
            viewModel.setLoadingStatus(false)
            binding.searchEditText.setText("")
        }

        binding.searchField.setEndIconOnClickListener {
            viewModel.setLoadingStatus(false)
            binding.searchEditText.setText("")
        }
    }

    private fun setObservers() {

        viewModel.searchHistory.observe(viewLifecycleOwner, Observer {
            searchHistoryAdapter.submitList(it)
        })

        viewModel.items.observe(viewLifecycleOwner, Observer {
            searchResultAdapter.submitList(it)
        })

        viewModel.searchLoadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                false -> {
                    binding.searchField.startIconDrawable = null
                    binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
                }
                else -> {
                    binding.searchField.setStartIconDrawable(R.drawable.ic_back_24dp)
                }
            }
        })
    }

    override fun onClick(historyItem: SearchHistoryModel) {
        binding.searchEditText.apply {
            setText(historyItem.searchedKeyword)
            onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onItemClick(view: View, item: Item) {

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        val cardDetailTransitionName = getString(R.string.video_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
        val directions = SearchFragmentDirections.actionSearchFragmentToDetailFragment(item)
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
            Snackbar.make(binding.root, menuItem.title ?: "", Snackbar.LENGTH_SHORT).show()
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