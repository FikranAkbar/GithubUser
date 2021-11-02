package com.chessporg.githubuser.ui.detail.following

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.databinding.FragmentFollowBinding
import com.chessporg.githubuser.ui.detail.DetailFragment
import com.chessporg.githubuser.ui.home.UserAdapter
import kotlinx.coroutines.flow.collect

class FollowingFragment : Fragment(R.layout.fragment_follow), UserAdapter.OnItemClickCallback {

    private val viewModel: FollowingViewModel by viewModels()
    private lateinit var binding: FragmentFollowBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailFragment.EXTRA_USERNAME).toString()

        viewModel.getUserFollowing(username)

        binding.apply {
            rvUsers.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                userAdapter = UserAdapter(this@FollowingFragment)
                adapter = userAdapter
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.followingEvent.collect { event ->
                when (event) {
                    is FollowingViewModel.FollowingEvent.Error -> {
                        showLoading(false)
                    }
                    is FollowingViewModel.FollowingEvent.Loading -> {
                        showLoading(true)
                    }
                    is FollowingViewModel.FollowingEvent.Success -> {
                        showLoading(false)
                        userAdapter.submitList(event.result)
                        Log.d("TEST", event.result.toString())
                    }
                }
            }
        }
    }

    private fun showLoading(bool: Boolean) {
        binding.apply {
            when (bool) {
                true -> {
                    shimmerLayout.visibility = View.VISIBLE
                    rvUsers.visibility = View.GONE
                }
                else -> {
                    shimmerLayout.visibility = View.GONE
                    rvUsers.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onItemClicked(user: UserResponse) {

    }
}