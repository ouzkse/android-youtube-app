package android.app.ouzkse.youtube.ui.settings

import android.app.ouzkse.youtube.databinding.SettingsBottomSheetLayoutBinding
import android.app.ouzkse.youtube.util.AppThemeHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: SettingsBottomSheetLayoutBinding
    val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsBottomSheetLayoutBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(
        savedInstanceState: Bundle?
    ) {
        super.onActivityCreated(savedInstanceState)
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
        setupSwitchComponents()
    }

    private fun setupSwitchComponents() {
        binding.switchTheme.isChecked =
            when (AppThemeHelper.getSelectedThemeMode(requireContext())) {
                AppCompatDelegate.MODE_NIGHT_NO -> false
                else -> true
            }

        binding.switchTheme.setOnCheckedChangeListener { switch, isChecked ->
            when (isChecked) {
                true -> AppThemeHelper.saveTheme(AppCompatDelegate.MODE_NIGHT_YES, requireContext())
                else -> AppThemeHelper.saveTheme(AppCompatDelegate.MODE_NIGHT_NO, requireContext())
            }
        }

        binding.btnClearSearchHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        fun newInstance() = SettingsBottomSheetFragment()
    }
}