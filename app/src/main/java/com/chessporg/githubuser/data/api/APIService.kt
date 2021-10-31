package com.chessporg.githubuser.data.api

import com.chessporg.githubuser.BuildConfig
import com.chessporg.githubuser.data.model.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    fun getUsersByName(
        @Query("q") query: String
    ): Call<UserSearchResponse>
}