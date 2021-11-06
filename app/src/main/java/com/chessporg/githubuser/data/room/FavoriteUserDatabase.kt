package com.chessporg.githubuser.data.room

import androidx.room.Database

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserDatabase {
    abstract fun favoriteUserDao(): FavoriteUserDao
}