package com.chessporg.githubuser.ui.detail.following

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

class FollowingViewModel : ViewModel() {

    private val followingEventChannel = Channel<FollowingEvent>()
    val followingEvent = followingEventChannel.receiveAsFlow()

    private fun onGetUserFollowingStarted() = viewModelScope.launch {
        followingEventChannel.send(FollowingEvent.Loading)
    }

    private fun onGetUserFollowingSuccess(result: ArrayList<UserResponse>) = viewModelScope.launch {
        followingEventChannel.send(FollowingEvent.Success(result))
    }

    private fun onGetUserFollowingFailed(message: String) = viewModelScope.launch {
        followingEventChannel.send(FollowingEvent.Error(message))
    }

    fun getUserFollowing(username: String) = viewModelScope.launch {
        onGetUserFollowingStarted()
        APIClient
            .service
            .getUserFollowing(username)
            .enqueue(object : Callback<ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserResponse>>,
                    response: Response<ArrayList<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        onGetUserFollowingSuccess(response.body()!!)
                    } else {
                        onGetUserFollowingFailed("Fetching Data Failed...")
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                    onGetUserFollowingFailed("Network Failed...")
                    throw t
                }
            })
    }

    sealed class FollowingEvent {
        object Loading : FollowingEvent()
        data class Success(val result: ArrayList<UserResponse>) : FollowingEvent()
        data class Error(val message: String) : FollowingEvent()
    }
}