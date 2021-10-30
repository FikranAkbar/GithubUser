package com.chessporg.githubuser.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val detailUserEventChannel = Channel<DetailUserEvent>()
    val detailUserEvent = detailUserEventChannel.receiveAsFlow()

    val user = state.get<User>("user")

    fun onBackClick() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.NavigateBackToHome)
    }

    fun onShareClick(userData: User) = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.ShareGithubUserData(userData))
    }

    sealed class DetailUserEvent {
        object NavigateBackToHome : DetailUserEvent()
        data class ShareGithubUserData(val userData: User) : DetailUserEvent()
    }
}