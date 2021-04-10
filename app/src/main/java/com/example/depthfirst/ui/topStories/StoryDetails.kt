package com.example.depthfirst.ui.topStories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.example.depthfirst.R
import com.example.depthfirst.Utils
import com.example.depthfirst.model.Item
import dev.chrisbanes.accompanist.glide.GlideImage

@Composable
fun StoryDetailsScreen(story: Item, comments: List<Item>, depths: List<Int>){
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.padding(16.dp)
    ) { 
        // Remember our own LazyListState
        val listState = rememberLazyListState()

        // Provide it to LazyColumn
        LazyColumn(state = listState) {
            item {
                StoryDetailsHeader(
                    title = story.title!!,
                    text = story.text,
                    url = story.url,
                    score = story.score!!,
                    descendants = story.descendants!!,
                    relativeTime = Utils.convertUnixToRelativeTime(story.time!!),
                    author = story.by!!
                )
            }
            itemsIndexed(comments) { index, commentItem ->
                CommentListItem(comment = commentItem, depth = depths[index])
            }
        }

    }
}

@Composable
fun StoryDetailsHeader(
    title: String,
    text: String?,
    url: String?,
    score: String,
    descendants: String,
    relativeTime: String,
    author: String
){
    Column {
        // Title of the post
        Text(
            text = title,
            style = MaterialTheme.typography.h4
        )

        text?.let {
            val text = HtmlCompat.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
            Text(
                text = text,
                style = MaterialTheme.typography.body1
            )
        }

        // Display favicon with base url and a clickable icon
        // that takes you to the original link.
        url?.let {
            FaviconAndUrl(
                url = it,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        StoryMetadata(
            score,
            descendants,
            relativeTime,
            author
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoryDetailsHeader(){
    StoryDetailsHeader(
        title = "Test Title",
        text = "This story is awesome!",
        url = "https://www.google.com/",
        score = "50",
        descendants = "5",
        relativeTime = "an hour ago",
        author = "Avin Sharma"
    )
}

@Composable
fun FaviconImage(url: String, modifier: Modifier){
    // Fetch base url to further use it to fetch favicon
    val faviconUrl = Utils.getFaviconUrl(url)

    GlideImage(
        data = faviconUrl,
        contentDescription = "My content description",
        loading = {
            Box(Modifier.matchParentSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        error = { error ->
            Log.e("Image Fetch Error!", error.toString())
            Icon(
                painter = painterResource(id = R.drawable.ic_default_favicon_24),
                contentDescription = "Favicon of the url ${Utils.getHostUrl(url)}"
            )
        },
        modifier = modifier
    )
}

@Composable
fun FaviconAndUrl(url: String, modifier: Modifier){
    val baseUrl = Utils.getHostUrl(url)
    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current


    Row(modifier = modifier) {
        FaviconImage(url = url, modifier = Modifier.size(24.dp))
        Text(text = baseUrl, modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_open_in_new_24),
            contentDescription = "Button that opens the story url",
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.clickable {
                startActivity(context, urlIntent, Bundle.EMPTY)
            }
        )

    }
}

@Composable
fun StoryMetadata(
    score: String,
    commentsCount: String,
    relativeTime: String,
    author: String
){
    Column() {
        Row() {
            Text(text = score, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.h6)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "p")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_comment_24),
                contentDescription = "Comment Icon")
            Text(text = commentsCount)
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "by $author | $relativeTime",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun CommentListItem(comment: Item, depth: Int) {
    if (comment.deleted == "true"){
        Card(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "deleted", Modifier.padding(start = 4.dp))
            }
        }
        return
    }
    val author = comment.by!!
    val relativeTime = Utils.convertUnixToRelativeTime(comment.time!!)
    val text = HtmlCompat.fromHtml(comment.text!!, FROM_HTML_MODE_LEGACY).toString().trim()

    CommentListItem(author = author, relativeTime = relativeTime, body = text, depth = depth)
}

@Composable
fun CommentListItem(author: String, relativeTime: String, body: String, depth: Int) {
    Card(modifier = Modifier.padding(start = (depth * 8).dp, top = 8.dp), elevation = 2.dp) {
        Column() {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "by $author | $relativeTime",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .padding(4.dp)
            ) {
                Spacer(modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.primary))
                Text(text = body, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewCommentListItem() {
    CommentListItem(
        author = "Avin",
        relativeTime = "1 hour ago",
        body = "This is a test comment",
        depth = 1
    )
}