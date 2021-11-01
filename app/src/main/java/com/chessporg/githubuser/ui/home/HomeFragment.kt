package com.chessporg.githubuser.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.databinding.FragmentHomeBinding
import com.chessporg.githubuser.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import okhttp3.internal.notifyAll

class HomeFragment : Fragment(R.layout.fragment_home), UserAdapter.OnItemClickCallback {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.apply {
            rvUsers.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                userAdapter = UserAdapter(this@HomeFragment)
                adapter = userAdapter
            }

            svUser.onQueryTextChanged {
                viewModel.searchQuery.postValue(it)
                Log.d("TEST", "Teks berubah")
            }
        }

        viewModel.getUserByName("a")

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            viewModel.getUserByName(it)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    is HomeViewModel.HomeEvent.Error -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is HomeViewModel.HomeEvent.LoadingQuery -> {

                    }
                    is HomeViewModel.HomeEvent.NavigateToDetailUser -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailFragment(event.username)
                        findNavController().navigate(action)
                    }
                    is HomeViewModel.HomeEvent.SuccessQuery -> {
                        userAdapter.submitList(event.result)
                        Log.d("TEST", "DATA MASUK")
                    }
                }
            }
        }
    }

    override fun onItemClicked(user: UserResponse) {
        viewModel.onUserSelected(user)
    }
}