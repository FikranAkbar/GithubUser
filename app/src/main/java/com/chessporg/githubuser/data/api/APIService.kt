package com.chessporg.githubuser.data.api

import com.chessporg.githubuser.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    suspend fun getUsersByName(
        @Query("q") query: String
    )
}