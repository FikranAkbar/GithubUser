package com.chessporg.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.room.FavoriteUser
import com.chessporg.githubuser.data.room.FavoriteUserDao
import com.chessporg.githubuser.data.room.FavoriteUserDatabase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoriteUserViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteUserDb: FavoriteUserDatabase = FavoriteUserDatabase.getInstance(application)
    private val favoriteUserDao: FavoriteUserDao = favoriteUserDb.favoriteUserDao()

    private val favoriteUserFlow = favoriteUserDao.getAllFavoriteUser()
    val favoriteUsers = favoriteUserFlow.asLiveData()

    private val favoriteUserEventChannel = Channel<FavoriteUserEvent>()
    val favoriteUserEvent = favoriteUserEventChannel.receiveAsFlow()

    fun onUserSelected(user: FavoriteUser) = viewModelScope.launch {
        favoriteUserEventChannel.send(FavoriteUserEvent.NavigateToDetailUser(user.login))
    }

    sealed class FavoriteUserEvent {
        data class NavigateToDetailUser(val username: String) : FavoriteUserEvent()
    }
}