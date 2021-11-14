package com.chessporg.githubuser.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserDetailResponse(
    @SerializedName("login")
    val username: String,
    val name: String,
    val location: String,
    val company: String,
    val id: Int,
    val avatar_url: String,
    val public_repos: Int,
    val bio: String?,
    val followers: Int,
    val following: Int,
    val created_at: Date,
)
