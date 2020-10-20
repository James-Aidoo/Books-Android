package com.questdev.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.questdev.books.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val book = intent.getParcelableExtra<Book>("Book")
        val binding = DataBindingUtil.setContentView<ActivityBookDetailBinding>(this,
            R.layout.activity_book_detail)
        binding.book = book
    }
}
