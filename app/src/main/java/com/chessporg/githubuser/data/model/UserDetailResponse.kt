package com.chessporg.githubuser.data.model

import java.util.*

data class UserDetailResponse(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val public_repos: Int,
    val bio: String,
    val followers: Int,
    val following: Int,
    val created_at: Date,
)
