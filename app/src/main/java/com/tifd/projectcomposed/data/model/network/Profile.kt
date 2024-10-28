package com.tifd.projectcomposed.data.model.network

import com.google.gson.annotations.SerializedName

data class Profile(
    @field:SerializedName("login") val login: String,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("followers") val followers: Int,
    @field:SerializedName("following") val followingCount: Int,
    @field:SerializedName("avatar_url") val avatarUrl: String
)
