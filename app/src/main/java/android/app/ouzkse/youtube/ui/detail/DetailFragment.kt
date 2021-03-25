package android.app.ouzkse.youtube.ui.detail

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.databinding.FragmentDetailBinding
import android.app.ouzkse.youtube.util.themeColor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var playerView: YouTubePlayerView
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            duration = 300.toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val argument = DetailFragmentArgs.fromBundle(requireArguments()).itemArg

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.setItem(item = argument)

        binding.btnClose.setOnClickListener {
            this.findNavController().navigateUp()
        }

        playerView = binding.player

        viewLifecycleOwner.lifecycle.addObserver(playerView)

        viewModel.item.observe(viewLifecycleOwner, Observer { item ->

            playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(item.id.videoId, 0F)
                }
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView.release()
    }
}
