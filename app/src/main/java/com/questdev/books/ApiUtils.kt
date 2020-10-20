package com.questdev.books

import android.media.ThumbnailUtils
import android.net.Uri
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class ApiUtils {
    companion object {
        private const val BASE_API_URL = "https://www.googleapis.com/books/v1/volumes"
        private const val API_KEY = "AIzaSyBFYA9MZRBr9Appg_vM61M1wE5Ha0Yp7Yw"
        private const val KEY = "key"
        private const val QUERY_PARAMETER_KEY = "q"
        private const val TITLE = "intitle:"
        private const val AUTHOR = "inauthor:"
        private const val PUBLISHER = "inpublisher:"
        private const val ISBN = "isbn:"
        
        fun buildUrl(title: String?) : URL {
            val uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build()
            var url: URL? = null
            try {
                url = URL(uri.toString())
            }catch (e: Exception){
                e.printStackTrace()
            }
            return url!!
        }

        fun buildUrl(title: String, author: String, publisher: String, isbn: String) : URL {
            var url: URL? = null

            val sb = StringBuilder()
            if (title.isNotEmpty()) sb.append("$TITLE$title+")
            if (author.isNotEmpty()) sb.append("$AUTHOR$author+")
            if (publisher.isNotEmpty()) sb.append("$PUBLISHER$publisher+")
            if (isbn.isNotEmpty()) sb.append("$ISBN$isbn+")
            sb.setLength(sb.length -1)

            val query = sb.toString()
            val uri = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build()

            try {
                url = URL(uri.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return url!!
        }

        @Throws(IOException::class)
        fun getJson(url: URL) : String? {
            val connection = url.openConnection()
            var result: String? = null
            try {
                val stream = connection.getInputStream()
                val scanner = Scanner(stream)
                scanner.useDelimiter("\\A")
                result = if (scanner.hasNext())
                    scanner.next()
                else
                    null
            } catch (e: Exception){
                Log.d("error", e.toString())
            }
            return result
        }

        fun getBooksFromJson(json: String) : ArrayList<Book> {
            val ID = "id"
            val TITLE = "title"
            val SUBTITLE = "subtitle"
            val AUTHORS = "authors"
            val PUBLISHER = "publisher"
            val PUBLISHED_DATE = "publishedDate"
            val DESCRIPTION = "description"
            val ITEMS = "items"
            val VOLUME_INFO = "volumeInfo"
            val IMAGELINKS = "imageLinks"
            val THUMBNAIL = "thumbnail"

            val books = ArrayList<Book>()
            try {
                val jsonBook = JSONObject(json)
                val arrayBooks = jsonBook.getJSONArray(ITEMS)
                val numberOfBooks = arrayBooks.length()

                for (i in 0 until numberOfBooks) {
                    val bookJson = arrayBooks.getJSONObject(i)
                    val volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO)

                    val authorNum = if (volumeInfoJson.isNull(AUTHORS)) 0; else volumeInfoJson.getJSONArray(AUTHORS).length()
                    val imageLinksJson = if (volumeInfoJson.has(IMAGELINKS)) volumeInfoJson.getJSONObject(IMAGELINKS) else JSONObject()
                    val authors = Array(authorNum){""}

                    for (j in 0 until authorNum) {
                        authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString()
                    }

                    val book = Book(
                        bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        if (volumeInfoJson.isNull(SUBTITLE)) ""; else volumeInfoJson.getString(SUBTITLE),
                        authors,
                        if (volumeInfoJson.isNull(PUBLISHER)) ""; else volumeInfoJson.getString(PUBLISHER),
                        if (volumeInfoJson.isNull(PUBLISHED_DATE)) ""; else volumeInfoJson.getString(PUBLISHED_DATE),
                        if (volumeInfoJson.isNull(DESCRIPTION)) ""; else volumeInfoJson.getString(DESCRIPTION),
                        if (imageLinksJson.isNull(THUMBNAIL)) ""; else imageLinksJson.getString(THUMBNAIL)
                    )

                    books.add(book)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return books
        }
    }
}