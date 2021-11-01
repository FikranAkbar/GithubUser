package com.chessporg.githubuser.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.api.APIClient
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.data.model.UserListResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val homeEventChannel = Channel<HomeEvent>()
    val homeEvent = homeEventChannel.receiveAsFlow()

    var searchQuery = MutableLiveData<String>()

    fun onUserSelected(user: User) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateToDetailUser(user))
    }

    private fun onApiCallStarted() = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.LoadingQuery)
    }

    private fun onApiCallSuccess(result: ArrayList<UserResponse>) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.SuccessQuery(result))
    }

    private fun onApiCallError(text: String) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.Error(text))
    }

    fun getUserByName(query: String) = viewModelScope.launch {
        onApiCallStarted()
        APIClient
            .service
            .getUsersByName(query)
            .enqueue(object : Callback<UserListResponse> {
                override fun onResponse(
                    call: Call<UserListResponse>,
                    response: Response<UserListResponse>
                ) {
                    if (response.isSuccessful) {
                        val queryResult = response.body()?.items!!
                        onApiCallSuccess(queryResult)
                    } else {
                        onApiCallError("Fetching Data Failed...")
                    }
                }

                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    onApiCallError("Network Failed...")
                    throw t
                }
            })
    }

    sealed class HomeEvent {
        data class NavigateToDetailUser(val user: User) : HomeEvent()
        data class SuccessQuery(val result: ArrayList<UserResponse>) : HomeEvent()
        object LoadingQuery : HomeEvent()
        data class Error(val message: String) : HomeEvent()
    }
}