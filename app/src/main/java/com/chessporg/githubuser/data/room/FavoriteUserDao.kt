package com.chessporg.githubuser.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser)

    @Delete
    suspend fun deleteFavoriteUser(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user_table")
    fun getAllFavoriteUser(): Flow<List<FavoriteUser>>
}