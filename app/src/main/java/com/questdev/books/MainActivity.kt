package com.questdev.books

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.net.URL
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val query = intent.getStringExtra("Query")

        try {
            val bookUrl = if (query == null || query.isEmpty()) {
                ApiUtils.buildUrl("cooking")
            } else {
                URL(query)
            }

            val apiTask = ApiAsyncTask()
            apiTask.execute(bookUrl)
            rvBooks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        } catch (e: Exception){
            Log.d("error", e.message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.book_list_menu, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)

        val recentList = SharedPrefUtils.getQueryList(this)
        val itemNum = recentList.size

        for (i in 0 until itemNum) {
            menu.add(Menu.NONE, i, Menu.NONE, recentList[i])
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_advanced_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                val position = item!!.itemId + 1
                val preferenceName = "${SharedPrefUtils.QUERY}$position"
                val query = SharedPrefUtils.getPreferenceString(this, preferenceName)
                val prefParams = (query.split("\\|"))
                val queryParams = Array(4) {""}

                for (i in prefParams.indices)
                    queryParams[i] = prefParams[i]

                val bookUrl = ApiUtils.buildUrl(
                    if (queryParams[0].isEmpty()) ""; else queryParams[0],
                    if (queryParams[1].isEmpty()) ""; else queryParams[1],
                    if (queryParams[2].isEmpty()) ""; else queryParams[2],
                    if (queryParams[3].isEmpty()) ""; else queryParams[3]
                )

                ApiAsyncTask().execute(bookUrl)
                true
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        try {
            val bookUrl = ApiUtils.buildUrl(query)
            ApiAsyncTask().execute(bookUrl)
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    inner class ApiAsyncTask : AsyncTask<URL, Void, String?>() {
        override fun doInBackground(vararg params: URL?): String? {
            val bookUrl = params[0]
            return ApiUtils.getJson(bookUrl!!)
        }

        override fun onPostExecute(result: String?) {
            if (result == null) {
                progress.visibility = View.INVISIBLE
                tvError.visibility = View.VISIBLE
                rvBooks.visibility = View.INVISIBLE
                return
            }
            else {
                progress.visibility = View.INVISIBLE
                rvBooks.visibility = View.VISIBLE
                tvError.visibility = View.INVISIBLE

                val books = ApiUtils.getBooksFromJson(result)
                val adapter = BooksListAdapter(books)
                rvBooks.adapter = adapter
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progress.visibility = View.VISIBLE
        }
    }
}
