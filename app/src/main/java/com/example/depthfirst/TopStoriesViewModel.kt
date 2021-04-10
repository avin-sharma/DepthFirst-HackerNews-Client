package com.example.depthfirst

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.depthfirst.model.Item
import kotlinx.coroutines.launch

class TopStoriesViewModel : ViewModel() {

    private val api: HackerNewsApiService = HackerNewsApi.retrofitService
    private var topStoriesIds: List<String> by mutableStateOf(listOf())
    var topStories: List<Item> by mutableStateOf(listOf())
        private set

    private var start: Int = 0
    private var end: Int = 20

    lateinit var clickedStory: Item

    var storyComments: List<Item> by mutableStateOf(listOf())
    var storyCommentsDepth: List<Int> = mutableListOf()

    init {
        fetchTopStories()
    }

    /**
     * A function that fetches ids of all the
     * top stories and fetches the first batch of
     * story items.
     */
    private fun fetchTopStories() {
        viewModelScope.launch {
            try {
                topStoriesIds = api.getTopStoriesItemIds()
                fetchTopStoriesItems()
            } catch (e: Exception) {
                Log.d("ViewModel:Fetch Top Stories Ids", e.toString())
            }
        }
    }

    /**
     * Fetches Top Stories of the ids ranging
     * from integer start to integer end.
     */
    private fun fetchTopStoriesItems() {
        viewModelScope.launch {
            try {
                for (id in topStoriesIds.subList(start, end)) {
                    val item = api.getItemFromId(id)
                    topStories = topStories + listOf(item)
                }
            } catch (e: Exception) {
                Log.d("ViewModel:Fetch Stories", e.toString())
            }
        }
    }

    /**
     * Fetch more stories when end of the list is reached.
     * Updates start and end variables.
     */
    private fun addStories() {
        start = end
        if (start >= topStoriesIds.size) {
            return
        }
        end += 20
        fetchTopStoriesItems()
    }

    /**
     * Function that checks if the given index is the
     * last element of the stories we have fetched. If
     * the last index is reached new stories are fetched
     * and added to the list.
     */
    fun checkEndOfList(index: Int) {
        if (index == topStories.size - 1) {
            addStories()
        }
    }

    fun onStoryClicked(item: Item) {
        clickedStory = item
        fetchStoryComments()
    }

    fun fetchStoryComments() {
        storyComments = listOf()
        storyCommentsDepth = listOf()

        clickedStory.kids?.let {
            viewModelScope.launch {
                for (id in it) {
                    fetchCommentItem(id, 0)
                }
            }
        }
    }

    private suspend fun fetchCommentItem(id: String, depth: Int) {
        try {
            val item = api.getItemFromId(id)
            storyComments = storyComments + listOf(item)
            storyCommentsDepth = storyCommentsDepth + listOf(depth)


            item.kids?.let {
                for (kId in item.kids) {
                    fetchCommentItem(kId, depth + 1)
                }
            }
        } catch (e: Exception) {
            Log.e("ViewModel:Fetch Comments", e.toString())
        }
    }
}