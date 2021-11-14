package com.chessporg.githubuser.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user_table")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val login: String,
    val avatar_url: String,
)
