package com.chessporg.githubuser.data.room

import androidx.room.Entity

@Entity(tableName = "favorite_user_table")
data class FavoriteUser(
    val id: Int,
    val login: String,
    val avatar_url: String
)
