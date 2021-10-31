package com.chessporg.githubuser.ui.home

import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(R.layout.fragment_home), UserAdapter.OnItemClickCallback {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    //region DataUser
    private lateinit var dataName: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var userList: ArrayList<User>
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        getData()

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            val userAdapter = UserAdapter(userList, this@HomeFragment)
            adapter = userAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.usersEvent.collect { event ->
                when (event) {
                    is HomeViewModel.UsersEvent.NavigateToDetailUser -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(event.user)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        viewModel.getUserByName("Fikran")
    }

    private fun getData() {
        dataName = resources.getStringArray(R.array.name)
        dataUsername = resources.getStringArray(R.array.username)
        dataLocation = resources.getStringArray(R.array.location)
        dataRepository = resources.getStringArray(R.array.repository)
        dataCompany = resources.getStringArray(R.array.company)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)

        userList = arrayListOf()
        for (i in dataName.indices) {
            val user = User(
                name = dataName[i],
                username = dataUsername[i],
                location = dataLocation[i],
                repository = dataRepository[i],
                company = dataCompany[i],
                followers = dataFollowers[i],
                following = dataFollowing[i],
                avatar = dataAvatar.getResourceId(i, -1),
            )
            userList.add(user)
        }
    }

    override fun onItemClicked(user: User) {
        viewModel.onUserSelected(user)
    }
}