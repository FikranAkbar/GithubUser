package com.chessporg.githubuser.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val usersEventChannel = Channel<UsersEvent>()
    val usersEvent = usersEventChannel.receiveAsFlow()

    fun onUserSelected(user: User) = viewModelScope.launch {
        usersEventChannel.send(UsersEvent.NavigateToDetailUser(user))
    }

    sealed class UsersEvent {
        data class NavigateToDetailUser(val user: User) : UsersEvent()
    }
}