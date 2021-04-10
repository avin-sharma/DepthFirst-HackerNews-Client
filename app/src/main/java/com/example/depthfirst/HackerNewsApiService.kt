package com.example.depthfirst

import com.example.depthfirst.model.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Class that defines all the interfaces for
 * the api calls we need. (Used for retrofit calls).
 */
private const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface HackerNewsApiService {
    @GET("topstories.json")
    suspend fun getTopStoriesItemIds(): List<String>

    @GET("item/{id}.json")
    suspend fun getItemFromId(@Path("id") id: String): Item
}

object HackerNewsApi {
    val retrofitService: HackerNewsApiService by lazy {
        retrofit.create(HackerNewsApiService::class.java)
    }
}