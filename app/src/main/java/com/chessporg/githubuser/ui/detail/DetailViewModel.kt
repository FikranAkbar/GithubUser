package com.chessporg.githubuser.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chessporg.githubuser.data.model.User

class DetailViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val user = state.get<User>("user")

}