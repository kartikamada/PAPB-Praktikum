package com.tifd.projectcomposed

import com.google.gson.annotations.SerializedName

data class Profile(
    @field:SerializedName("login") val login: String,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("followers") val followers: Int,
    @field:SerializedName("following") val followingCount: Int,
    @field:SerializedName("avatar_url") val avatarUrl: String
)
