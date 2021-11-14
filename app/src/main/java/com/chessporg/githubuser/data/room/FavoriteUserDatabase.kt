package com.chessporg.githubuser.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        private var INSTANCE : FavoriteUserDatabase? = null

        fun getInstance(context: Context): FavoriteUserDatabase {
            if (INSTANCE != null) {
                return INSTANCE as FavoriteUserDatabase
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserDatabase::class.java,
                        "favorite_user_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}