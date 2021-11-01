package com.chessporg.githubuser.data.api

import com.chessporg.githubuser.BuildConfig
import com.chessporg.githubuser.data.model.UserDetailResponse
import com.chessporg.githubuser.data.model.UserListResponse
import com.chessporg.githubuser.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    fun getUsersByName(
        @Query("q") query: String
    ): Call<UserListResponse>

    @GET("users/{user}")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    fun getUserDetail(
        @Path("user") user: String
    ): Call<UserDetailResponse>

    @GET("users/{user}/followers")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    fun getUserFollowers(
        @Path("user") user: String
    ): Call<ArrayList<UserResponse>>

    @GET("users/{user}/following")
    @Headers("Authorization: token ${BuildConfig.GithubUserApi}")
    fun getUserFollowing(
        @Path("user") user: String
    ): Call<ArrayList<UserResponse>>
}