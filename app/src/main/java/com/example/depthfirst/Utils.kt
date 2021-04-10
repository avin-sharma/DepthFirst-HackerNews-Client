package com.example.depthfirst

import android.net.Uri
import java.net.URL


object Utils {
    fun convertUnixToRelativeTime(time: String): String{
        val longTime = time.toLong() * 1000
        val currentTime = System.currentTimeMillis()

        val SECOND_MILLIS = 1000;
        val MINUTE_MILLIS = 60 * SECOND_MILLIS;
        val HOUR_MILLIS = 60 * MINUTE_MILLIS;
        val DAY_MILLIS = 24 * HOUR_MILLIS;

        val timeDifference = currentTime - longTime

        when {
            timeDifference < MINUTE_MILLIS ->
                return "just now"
            timeDifference < 2 * MINUTE_MILLIS ->
                return " a minute ago"
            timeDifference < 50 * MINUTE_MILLIS ->
                return "${timeDifference / MINUTE_MILLIS} minutes ago"
            timeDifference < 90 * MINUTE_MILLIS ->
                return "an hour ago"
            timeDifference < 24 * HOUR_MILLIS ->
                return "${timeDifference / HOUR_MILLIS} hours ago"
            timeDifference < 48 * HOUR_MILLIS ->
                return "yesterday"
            else ->
                return "${timeDifference / DAY_MILLIS} days ago"
        }
    }

    fun getHostUrl(stringUrl: String): String {
        val url = URL(stringUrl)
        return url.host
    }

    fun getFaviconUrl(stringUrl: String): String {
        val uri = Uri.parse("https://www.google.com/s2/favicons")
            .buildUpon()
            .appendQueryParameter("domain", getHostUrl(stringUrl))
            .build()

        return uri.toString()
    }
}