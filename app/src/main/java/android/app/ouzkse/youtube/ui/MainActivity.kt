package android.app.ouzkse.youtube.ui

import android.app.ouzkse.youtube.R
import android.app.ouzkse.youtube.databinding.ActivityMainBinding
import android.app.ouzkse.youtube.util.setupWithNavController
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentNavigationController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {

        val navigationGraphIds = listOf(
            R.navigation.home_navigation_graph,
            R.navigation.search_navigation_graph,
            R.navigation.library_navigation_graph
        )

        val navController = binding.bottomNavigationBar.setupWithNavController(
            navigationGraphIds,
            supportFragmentManager,
            binding.navHostFragment.id,
            intent
        )

        currentNavigationController = navController
    }

    override fun onSupportNavigateUp(): Boolean =
        currentNavigationController?.value?.navigateUp() ?: false

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}