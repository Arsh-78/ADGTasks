package com.example.taskthree.utils

import com.example.taskthree.models.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "1d513a1fe277460ba9e3336197662571"
interface NewsInterface {
    @GET("v2/top-headlines?apikey=$API_KEY")
    fun getHeadline(@Query("country") country: String, @Query("page") page: Int) : Call<News>
}
object NewsService{
    val newsInstance: NewsInterface
    init {
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(NewsInterface::class.java)
    }
}