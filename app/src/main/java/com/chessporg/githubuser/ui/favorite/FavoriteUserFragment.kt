package com.chessporg.githubuser.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.databinding.FragmentFavoriteUserBinding
import com.chessporg.githubuser.ui.home.UserAdapter
import com.chessporg.githubuser.utils.mapFavoriteUsersToUserResponses
import com.chessporg.githubuser.utils.mapUserResponseToFavoriteUser
import kotlinx.coroutines.flow.collect

class FavoriteUserFragment : Fragment(R.layout.fragment_favorite_user),
    UserAdapter.OnItemClickCallback {
    private lateinit var binding: FragmentFavoriteUserBinding
    private val viewModel: FavoriteUserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteUserBinding.bind(view)

        binding.apply {
            rvUsers.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                userAdapter = UserAdapter(this@FavoriteUserFragment)
                adapter = userAdapter
            }

            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewModel.favoriteUsers.observe(viewLifecycleOwner) {
            val list = mapFavoriteUsersToUserResponses(it)
            userAdapter.submitList(list)
            Log.d("TAG", list.toString())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.favoriteUserEvent.collect { event ->
                when (event) {
                    is FavoriteUserViewModel.FavoriteUserEvent.NavigateToDetailUser -> {
                        val action =
                            FavoriteUserFragmentDirections.actionFavoriteUserFragmentToDetailFragment(
                                event.username
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemClicked(user: UserResponse) {
        viewModel.onUserSelected(mapUserResponseToFavoriteUser(user))
    }
}