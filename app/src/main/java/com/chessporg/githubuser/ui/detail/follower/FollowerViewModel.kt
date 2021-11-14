package com.chessporg.githubuser.ui.detail.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.api.APIClient
import com.chessporg.githubuser.data.model.UserResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {

    private val followerEventChannel = Channel<FollowerEvent>()
    val followerEvent = followerEventChannel.receiveAsFlow()

    private fun onGetUserFollowerStarted() = viewModelScope.launch {
        followerEventChannel.send(FollowerEvent.Loading)
    }

    private fun onGetUserFollowerSuccess(result: ArrayList<UserResponse>) = viewModelScope.launch {
        followerEventChannel.send(FollowerEvent.Success(result))
    }

    private fun onGetUserFollowerFailed(message: String) = viewModelScope.launch {
        followerEventChannel.send(FollowerEvent.Error(message))
    }

    fun onGithubUserItemClick(user: UserResponse) = viewModelScope.launch {
        followerEventChannel.send(FollowerEvent.NavigateToDetailFragment(user))
    }

    fun getUserFollower(username: String) = viewModelScope.launch {
        onGetUserFollowerStarted()
        APIClient
            .service
            .getUserFollowers(username)
            .enqueue(object : Callback<ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserResponse>>,
                    response: Response<ArrayList<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        onGetUserFollowerSuccess(response.body()!!)
                    } else {
                        onGetUserFollowerFailed("Fetching Data Failed...")
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                    onGetUserFollowerFailed("Network Failed...")
                    throw t
                }
            })
    }

    sealed class FollowerEvent {
        object Loading : FollowerEvent()
        data class Success(val result: ArrayList<UserResponse>) : FollowerEvent()
        data class Error(val message: String) : FollowerEvent()
        data class NavigateToDetailFragment(val user: UserResponse) : FollowerEvent()
    }
}