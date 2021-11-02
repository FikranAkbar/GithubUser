package com.chessporg.githubuser.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.api.APIClient
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.data.model.UserDetailResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(
    state: SavedStateHandle
) : ViewModel() {

    private val detailUserEventChannel = Channel<DetailUserEvent>()
    val detailUserEvent = detailUserEventChannel.receiveAsFlow()

    val user = state.get<String>("user")

    fun onBackClick() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.NavigateBackToHome)
    }

    fun onShareClick(userData: User) = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.ShareGithubUserData(userData))
    }

    private fun onGetUserDetailStarted() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.Loading)
    }

    private fun onGetUserDetailSuccess(result: UserDetailResponse) = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.Success(result))
    }

    private fun onGetUserDetailFailed(message: String) = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.Error(message))
    }

    fun getUserDetail(username: String) = viewModelScope.launch {
        onGetUserDetailStarted()
        APIClient
            .service
            .getUserDetail(username)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        onGetUserDetailSuccess(response.body()!!)
                    } else {
                        onGetUserDetailFailed("Fetching Data Failed...")
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    onGetUserDetailFailed("Network Failed...")
                    throw t
                }
            })
    }

    sealed class DetailUserEvent {
        object NavigateBackToHome : DetailUserEvent()
        data class ShareGithubUserData(val userData: User) : DetailUserEvent()
        object Loading : DetailUserEvent()
        data class Success(val data: UserDetailResponse) : DetailUserEvent()
        data class Error(val message: String) : DetailUserEvent()
    }
}