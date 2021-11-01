package com.chessporg.githubuser.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login")
    val username: String,
    val id: Int,
    val avatar_url: String
)
