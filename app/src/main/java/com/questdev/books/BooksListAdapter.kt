package com.questdev.books

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BooksListAdapter(val books: ArrayList<Book>) : RecyclerView.Adapter<BooksListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAuthors: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvPublisher: TextView = itemView.findViewById(R.id.tvPublisher)
        private val tvPublishedDate: TextView = itemView.findViewById(R.id.tvPublishedDate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val selectedBook = books[position]
                val intent = Intent(it.context, BookDetailActivity::class.java)
                intent.putExtra("Book", selectedBook)

                it.context.startActivity(intent)
            }
        }

        fun bind(book: Book) {
            tvAuthors.text = book.authors
            tvTitle.text = book.title
            tvPublisher.text = book.publisher
            tvPublishedDate.text = book.publishedDate
        }
    }
}