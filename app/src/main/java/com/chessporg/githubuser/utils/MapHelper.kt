package com.chessporg.githubuser.utils

import com.chessporg.githubuser.data.model.UserDetailResponse
import com.chessporg.githubuser.data.room.FavoriteUser

fun mapUserDetailResponseToFavoriteUser(user: UserDetailResponse): FavoriteUser {
    return FavoriteUser(
        login = user.username,
        avatar_url = user.avatar_url,
        id = user.id
    )
}