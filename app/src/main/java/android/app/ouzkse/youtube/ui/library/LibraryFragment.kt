package android.app.ouzkse.youtube.ui.library

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.databinding.FragmentLibraryBinding
import android.app.ouzkse.youtube.ui.common.LibraryMenuItemIds
import android.app.ouzkse.youtube.ui.settings.SettingsBottomSheetFragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = buildContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(layoutInflater, container, false)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_library)
            menu.findItem(R.id.settings).setOnMenuItemClickListener {
                openBottomSheet(); true
            }
        }

        binding.btnFavourites.setOnClickListener {
            val item = LibraryMenuItemModel(
                LibraryMenuItemIds.FAVOURITE,
                getString(R.string.library_menu_favourite)
            )
            navigateToDetail(it, item)
        }

        binding.btnWatchLater.setOnClickListener {
            val item = LibraryMenuItemModel(
                LibraryMenuItemIds.WATCH_LATER,
                getString(R.string.library_menu_watch_later)
            )
            navigateToDetail(it, item)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun openBottomSheet() {
        SettingsBottomSheetFragment.newInstance().show(
            requireActivity().supportFragmentManager,
            SettingsBottomSheetFragment.TAG
        )
    }

    private fun navigateToDetail(view: View, itemModel: LibraryMenuItemModel) {
        val detailTransitionName = getString(R.string.library_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to detailTransitionName)
        val directions = LibraryFragmentDirections
            .actionLibraryFragmentToLibraryDetailFragment(itemModel)
        this.findNavController().navigate(directions, extras)
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
}