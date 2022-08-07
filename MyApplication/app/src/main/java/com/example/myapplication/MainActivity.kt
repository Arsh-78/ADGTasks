package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiInterface.create().getBlogs()
        val mtext : TextView = findViewById(R.id.test)

        apiInterface.enqueue(object : Callback<List<Blog>>{
            override fun onResponse(call: Call<List<Blog>>, response: Response<List<Blog>>) {
                Log.d("Success",response.body().toString())
                mtext.setText(response.body().toString())
            }

            override fun onFailure(call: Call<List<Blog>>, t: Throwable) {
                Log.d("Nani","Eroor 404")
            }

        })
    }
}