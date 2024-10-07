package com.tifd.projectcomposed
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
        @GET("users/{username}")
        suspend fun getDetailUser(@Path("username") username:String) : Profile
    }