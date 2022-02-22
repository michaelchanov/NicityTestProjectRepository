package com.nicity.nicitytestproject.presentation.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nicity.nicitytestproject.R
import com.nicity.nicitytestproject.databinding.FragmentNavigationBinding
import com.nicity.nicitytestproject.presentation.feed.view.FeedFragment
import com.nicity.nicitytestproject.presentation.liked.view.LikedNewsFragment
import com.nicity.nicitytestproject.utils.delegates.viewBinding

class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    companion object {
        const val NAVIGATION_FROM_MENU_EXTRA_KEY = "NAVIGATION_FROM_MENU_EXTRA_KEY"
    }

    private val binding: FragmentNavigationBinding by viewBinding(FragmentNavigationBinding::bind)

    override fun onResume() {
        super.onResume()
        with(binding.bottomNavigation) {
            selectedItemId = R.id.feed_tab
            foregroundTintList = null
            itemIconTintList = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.feed_tab -> {
                    changeFragment(fragment = FeedFragment())
                }
                R.id.liked_tab -> {
                    changeFragment(fragment = LikedNewsFragment())
                }
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        try {
            val bundle = Bundle()
            bundle.putBoolean(NAVIGATION_FROM_MENU_EXTRA_KEY, true)

            fragment.arguments = bundle

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.navigationFragmentContainer, fragment)
                .commit()
        } catch (ignored: IllegalStateException) {
        }
    }
}