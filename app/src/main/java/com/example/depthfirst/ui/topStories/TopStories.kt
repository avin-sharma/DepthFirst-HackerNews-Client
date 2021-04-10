package com.example.depthfirst.ui.topStories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.depthfirst.R
import com.example.depthfirst.TopStoriesViewModel
import com.example.depthfirst.Utils
import com.example.depthfirst.model.Item
import com.example.depthfirst.ui.theme.DepthFirstTheme

@Composable
fun TopStoriesScreen(navController: NavController,
                     topStoriesViewModel: TopStoriesViewModel
) {
    Surface(color = MaterialTheme.colors.background) {
        // https://stackoverflow.com/questions/16120697/kotlin-how-to-pass-a-function-as-parameter-to-another
        StoryList(
            storyItems = topStoriesViewModel.topStories,
            topStoriesViewModel::checkEndOfList,
            topStoriesViewModel::onStoryClicked,
            navController
        )
    }
}

@Composable
fun StoryList(
    storyItems: List<Item>,
    checkEndOfList: (index: Int) -> Unit,
    onItemClicked: (item: Item) -> Unit,
    navController: NavController
) {
    // Remember our own LazyListState
    val listState = rememberLazyListState()

    // Provide it to LazyColumn
    @OptIn(ExperimentalFoundationApi::class)
    LazyColumn(state = listState) {
        // Header
        stickyHeader {
            StoryListHeader()
        }

        // List of items
        itemsIndexed(storyItems) { index, item ->
            StoryListItem(item = item, onItemClicked, navController)
            // Load more items
            checkEndOfList(index)

            // TODO: Add a boolean for loading
            // TODO: Show a loading composable while the boolean is true

        }
    }
}

@Composable
private fun StoryListHeader() {
    Surface(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    ) {
        Text(
            text = "Top Stories",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StoryListItem(
    item: Item,
    onItemClicked: (item: Item) -> Unit,
    navController: NavController
) {
    StoryListItem(
        score = item.score!!,
        title = item.title!!,
        author = item.by!!,
        relativeTime = Utils.convertUnixToRelativeTime(item.time!!),
        commentsCount = item.descendants,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onItemClicked(item)
                navController.navigate("storyDetails")
            }
    )
}


@Composable
fun StoryScore(score: String){
    Text(
        text = score,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(8.dp)
            .width(50.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStoryScore(){
    StoryScore(score = "100")
}

@Composable
fun StoryTitleAndMetadata(title: String, author: String, relativeTime: String, modifier: Modifier){
    Column(modifier = modifier) {
        // Title of the story
        Text(text = title, style = MaterialTheme.typography.subtitle1)

        // Author and Time metadata
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "by $author | $relativeTime",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoryTitleAndMetadata(){
    StoryTitleAndMetadata(
        title = "Test Title",
        author = "Avin",
        relativeTime = "10 hours ago",
        modifier = Modifier
    )
}

@Composable
fun StoryCommentIconAndCount(count: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_comment_24),
            contentDescription = "Comment Icon",
            tint = MaterialTheme.colors.onSurface
        )
        Text(text = count, modifier = Modifier.align(Alignment.End))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoryCommentIconAndCount(){
    StoryCommentIconAndCount(count = "20")
}

@Composable
fun StoryListItem(
    score: String,
    title: String,
    author: String,
    relativeTime: String,
    commentsCount: String?,
    modifier: Modifier
){
    Card(modifier = modifier){
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Score / Points
            StoryScore(score)

            //  Title + metadata
            StoryTitleAndMetadata(
                title = title,
                author = author,
                relativeTime = relativeTime,
                modifier = Modifier.weight(1f)
            )

            // Comment icon + comment count
            var count = "0"
            commentsCount?.let {
                count = it
            }
            StoryCommentIconAndCount(count = count)
        }
    }
}


@Preview
@Composable
fun PreviewStoryListItem(){
    StoryListItem(
        score = "10",
        title = "Test Title",
        author = "Avin",
        relativeTime = "10 hours ago",
        commentsCount = "20",
        modifier = Modifier
    )
}