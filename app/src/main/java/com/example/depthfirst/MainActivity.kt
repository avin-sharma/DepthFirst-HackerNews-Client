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

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<TopStoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "topStories") {
                composable("topStories") { TopStoriesScreen(mainViewModel) }
            }
        }
    }
}

@Composable
fun TopStoriesScreen(topStoriesViewModel: TopStoriesViewModel) {
    DepthFirstTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            // https://stackoverflow.com/questions/16120697/kotlin-how-to-pass-a-function-as-parameter-to-another
            StoryList(
                storyItems = topStoriesViewModel.topStories,
                topStoriesViewModel::checkEndOfList
            )
        }
    }
}

@Composable
fun StoryListItem(item: Item) {
    Surface(modifier = Modifier.padding(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .width(40.dp)
            ) {
                Text(
                    text = "${item.score}",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                item.title?.let { Text(text = it, style = MaterialTheme.typography.subtitle1) }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = "by ${item.by} | ${Utils.convertUnixToRelativeTime(item.time!!)}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_comment_24),
                    contentDescription = "Comment Icon",
                    modifier = Modifier.align(Alignment.End)
                )
                Text(text = "${item.descendants}", modifier = Modifier.align(Alignment.End))
            }
        }
    }

}

@Composable
fun StoryList(storyItems: List<Item>, checkEndOfList: (index: Int) -> Unit) {
    // Remember our own LazyListState
    val listState = rememberLazyListState()

    // Provide it to LazyColumn
    LazyColumn(state = listState) {
        itemsIndexed(storyItems) { index, item ->
            StoryListItem(item = item)
            // DONE: Load more items
            checkEndOfList(index)

            // TODO: Add a boolean for loading
            // TODO: Show a loading composable while the boolean is true

        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewStoryListItem() {
    StoryListItem(
        item = Item(
            "1",
            title = "Test Title",
            score = "100",
            by = "Augmenter",
            time = "1615685265",
            descendants = "35",
            deleted = null,
            type = null,
            text = null,
            parent = null,
            dead = null,
            kids = null,
            parts = null,
            poll = null,
            url = null
        )
    )
}