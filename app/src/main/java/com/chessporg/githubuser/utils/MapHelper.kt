package com.chessporg.githubuser.utils

import com.chessporg.githubuser.data.model.UserDetailResponse
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.data.room.FavoriteUser

fun mapUserDetailResponseToFavoriteUser(user: UserDetailResponse): FavoriteUser {
    return FavoriteUser(
        login = user.username,
        avatar_url = user.avatar_url,
        id = user.id
    )
}

fun mapFavoriteUsersToUserResponses(users: List<FavoriteUser>): List<UserResponse> {
    return users.map {
        UserResponse(it.login, it.id, it.avatar_url)
    }
}

fun mapUserResponseToFavoriteUser(user: UserResponse): FavoriteUser {
    user.apply {
        return FavoriteUser(id, username, avatar_url)
    }
}