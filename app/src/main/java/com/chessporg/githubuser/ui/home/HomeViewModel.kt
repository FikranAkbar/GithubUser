package com.chessporg.githubuser.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.api.APIClient
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.data.model.UserSearchResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val usersEventChannel = Channel<UsersEvent>()
    val usersEvent = usersEventChannel.receiveAsFlow()

    val queryResult = state.getLiveData("queryResult", ArrayList<UserResponse>())

    fun onUserSelected(user: User) = viewModelScope.launch {
        usersEventChannel.send(UsersEvent.NavigateToDetailUser(user))
    }

    fun getUserByName(query: String) = viewModelScope.launch {
        APIClient
            .service
            .getUsersByName(query)
            .enqueue(object : Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        queryResult.postValue(response.body()?.items!!)
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    throw t
                }
            })
    }

    sealed class UsersEvent {
        data class NavigateToDetailUser(val user: User) : UsersEvent()
    }
}