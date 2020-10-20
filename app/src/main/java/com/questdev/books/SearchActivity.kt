package com.questdev.books

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    fun onSearch(view: View) {
        val title = etTitle.text.toString().trim()
        val author = etAuthor.text.toString().trim()
        val publisher = etPublisher.text.toString().trim()
        val isbn = etISBN.text.toString().trim()

        if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_search_data), Toast.LENGTH_LONG).show()
        }
        else {
            val queryUrl =  ApiUtils.buildUrl(title, author, publisher, isbn)

            var position = SharedPrefUtils.getPreferenceInt(this, SharedPrefUtils.POSITION)
            if (position == 0 || position == 5)
                position = 1
            else
                position++

            val key = "${SharedPrefUtils.QUERY}${position}"
            val value = "$title|$author|$publisher|$isbn"

            SharedPrefUtils.setPreferenceString(this, key, value)
            SharedPrefUtils.setPreferenceInt(this, SharedPrefUtils.POSITION, position)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Query", queryUrl.toString())
            startActivity(intent)
        }
    }
}
