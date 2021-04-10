package com.example.depthfirst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.depthfirst.model.Item
import com.example.depthfirst.ui.theme.DepthFirstTheme
import com.example.depthfirst.ui.topStories.StoryDetailsScreen
import com.example.depthfirst.ui.topStories.TopStoriesScreen

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<TopStoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DepthFirstTheme {
                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = "topStories") {
                    composable("topStories") {
                        TopStoriesScreen(navController, mainViewModel)
                    }
                    composable("storyDetails") {
                        StoryDetailsScreen(
                            mainViewModel.clickedStory,
                            mainViewModel.storyComments,
                            mainViewModel.storyCommentsDepth
                        )
                    }
                }
            }
        }
    }
}

