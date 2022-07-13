package com.example.taskthree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskthree.models.News
import com.example.taskthree.utils.NewsAdapter
import com.example.taskthree.utils.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
    }

    private fun getData() {
        val news = NewsService.newsInstance.getHeadline("in", 1)
        news.enqueue(object : Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news!= null){
                    adapter = NewsAdapter(this@MainActivity,news.articles)
                    findViewById<RecyclerView>(R.id.newsRecyclerView).adapter =adapter
                    findViewById<RecyclerView>(R.id.newsRecyclerView).layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occured", Toast.LENGTH_LONG).show()
            }
        })
    }
}