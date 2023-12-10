package com.ismin.treewonder

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TreeService {
    @GET("trees")
    fun getAllTrees(): Call<List<Tree>>
    @POST("trees")
    fun createTree(@Body tree : Tree): Call<Tree>
}