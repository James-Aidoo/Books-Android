package com.questdev.books

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtils private constructor() {

    companion object {
        private val PREF_NAME = "BooksPreferences"
        val POSITION = "position"
        val QUERY = "query"

        private fun getPrefs(context: Context) : SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun getPreferenceString(context: Context, key: String) : String {
            return getPrefs(context).getString(key, "")!!
        }

        fun getPreferenceInt(context: Context, key: String) : Int {
            return getPrefs(context).getInt(key, 0)
        }

        fun setPreferenceString(context: Context, key: String, value: String) {
            val editor = getPrefs(context).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun setPreferenceInt(context: Context, key: String, value: Int) {
            val editor = getPrefs(context).edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getQueryList(context: Context) : ArrayList<String> {
            val queryList: ArrayList<String> = ArrayList()
            for (i in 1..5) {
                var query = getPreferenceString(context, "$QUERY$i")
                if (query.isNotEmpty()) {
                    query = query.replace("|"," ")
                    queryList.add(query.trim())
                }
            }
            return queryList
        }
    }
}