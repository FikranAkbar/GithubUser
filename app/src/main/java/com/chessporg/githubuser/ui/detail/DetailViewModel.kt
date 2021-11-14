package com.chessporg.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chessporg.githubuser.data.api.APIClient
import com.chessporg.githubuser.data.model.UserDetailResponse
import com.chessporg.githubuser.data.room.FavoriteUserDao
import com.chessporg.githubuser.data.room.FavoriteUserDatabase
import com.chessporg.githubuser.utils.mapUserDetailResponseToFavoriteUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(
    application: Application,
    state: SavedStateHandle
) : AndroidViewModel(application) {

    private lateinit var userDetailResponse: UserDetailResponse

    private var favoriteUserDb: FavoriteUserDatabase = FavoriteUserDatabase.getInstance(application)
    private var favoriteUserDao: FavoriteUserDao = favoriteUserDb.favoriteUserDao()

    private val detailUserEventChannel = Channel<DetailUserEvent>()
    val detailUserEvent = detailUserEventChannel.receiveAsFlow()

    val username = state.get<String>("username")

    fun onBackClick() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.NavigateBackToHome)
    }

    fun onShareClick(username: String) = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.ShareGithubUserData(username))
    }

    fun onFavoriteClick(isFavorited: Boolean) = viewModelScope.launch {
        val favoriteUser = mapUserDetailResponseToFavoriteUser(userDetailResponse)
        if (isFavorited) {
            favoriteUserDao.insertFavoriteUser(favoriteUser)
        } else {
            favoriteUserDao.deleteFavoriteUser(favoriteUser)
        }

        detailUserEventChannel.send(DetailUserEvent.ChangeFavoriteState)
    }

    fun onInitFragment() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.InitDetailFragment(username!!))
    }

    private fun onGetUserDetailStarted() = viewModelScope.launch {
        detailUserEventChannel.send(DetailUserEvent.Loading)
    }

    private fun onGetUserDetailSuccess(result: UserDetailResponse) = viewModelScope.launch {
        userDetailResponse = result
        detailUserEventChannel.send(DetailUserEvent.Success(userDetailResponse))
        val isUserAlreadyFavorite = favoriteUserDao.isUserFavorited(userDetailResponse.id) > 0
        detailUserEventChannel.send(DetailUserEvent.InitFavoriteState(isUserAlreadyFavorite))
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
        data class InitDetailFragment(val username: String) : DetailUserEvent()
        data class InitFavoriteState(val isUserAlreadyFavorite: Boolean) : DetailUserEvent()
        object NavigateBackToHome : DetailUserEvent()
        data class ShareGithubUserData(val username: String) : DetailUserEvent()
        object ChangeFavoriteState : DetailUserEvent()
        object Loading : DetailUserEvent()
        data class Success(val data: UserDetailResponse) : DetailUserEvent()
        data class Error(val message: String) : DetailUserEvent()
    }
}