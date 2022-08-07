package com.example.myapplication

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface ApiInterface {

    @GET(".")
    fun getBlogs() : Call<List<Blog>>

    companion object {
        var baseUrl = "https://adg-rec-task-1.herokuapp.com/"
        fun create() : ApiInterface{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}