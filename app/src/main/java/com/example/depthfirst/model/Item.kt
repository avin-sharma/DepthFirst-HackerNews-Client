package com.example.depthfirst.model

data class Item(
    val id: String,
    val deleted: String?,
    val type: String?,
    val by: String?,
    val time: String?,
    val text: String?,
    val dead: String?,
    val parent: String?,
    val poll: String?,
    val kids: List<String>?,
    val url: String?,
    val score: String?,
    val title: String?,
    val parts: List<String>?,
    val descendants: String?
)