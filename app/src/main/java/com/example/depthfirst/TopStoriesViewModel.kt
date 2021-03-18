package com.example.depthfirst

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.depthfirst.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TopStoriesViewModel: ViewModel() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://hacker-news.firebaseio.com/v0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val hackerNewsApi: HackerNewsApiService = retrofit.create(HackerNewsApiService::class.java)

    private var topStoriesIds: List<String> by mutableStateOf(listOf())

    var topStories: List<Item> by mutableStateOf(listOf())
        private set

    var start: Int = 0
    var end: Int = 20

    init {
        fetchTopStories()
    }

    private fun fetchTopStories(){
        hackerNewsApi.getTopStoriesItemIds().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                topStoriesIds = response.body()!!
                fetchTopStoriesItems()
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("ViewModel", t.toString())
            }

        })
    }

    fun fetchTopStoriesItems(){
        for (id in topStoriesIds.subList(start, end)){
            hackerNewsApi.getItemFromId(id).enqueue(object : Callback<Item> {
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    topStories = topStories + listOf(response.body()!!)
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.d("ViewModel:Fetch Stories", t.toString())
                }
            })
        }
    }

    fun addStories(){
        start = end
        if (start >= topStoriesIds.size){
            return
        }
        end += 20
        fetchTopStories()
    }

    fun checkEndOfList(index: Int){
        if (index == topStories.size - 1){
            addStories()
        }
    }
}