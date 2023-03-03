package net.noliaware.yumi.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.feature_profile.presentation.views.ProfileView

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileView: ProfileView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.profile_layout, false)?.apply {
            profileView = this as ProfileView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = profileView?.getViewPager

        UserProfileFragmentStateAdapter(childFragmentManager, lifecycle).apply {
            viewPager?.adapter = this
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileView = null
    }
}

class UserProfileFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserProfileDataFragment()
            }
            1 -> {
                UsedCategoriesFragment()
            }
            else -> {
                CancelledCategoriesFragment()
            }
        }
    }
}