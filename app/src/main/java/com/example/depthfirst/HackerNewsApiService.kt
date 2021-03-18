package com.example.depthfirst

import com.example.depthfirst.model.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Class that defines all the interfaces for
 * the api calls we need. (Used for retrofit calls).
 */
interface HackerNewsApiService {
    @GET("topstories.json")
    fun getTopStoriesItemIds(): Call<List<String>>

    @GET("item/{id}.json")
    fun getItemFromId(@Path("id") id: String): Call<Item>
}