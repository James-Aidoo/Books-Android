package com.questdev.books

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

data class Book(
    val id: String,
    val title: String,
    val subtitle: String,
    val authors: String,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val thumbnail: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    constructor(
        id: String,
        title: String,
        subtitle: String,
        authors: Array<String>,
        publisher: String,
        publishedDate: String,
        description: String,
        thumbnail: String
    ) : this(id, title, subtitle, TextUtils.join(", ", authors), publisher, publishedDate, description, thumbnail)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(authors)
        parcel.writeString(publisher)
        parcel.writeString(publishedDate)
        parcel.writeString(description)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }

        @BindingAdapter("imageUrl") @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String) {
            if (imageUrl.isNotEmpty()){
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_library_books_black_24dp)
                    .into(view, object : Callback{
                        override fun onSuccess() {
//                        Toast.makeText(view.context, "Loaded Image", Toast.LENGTH_LONG).show()
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso", e?.message)
                        }
                    })
            }
            else
                view.setBackgroundResource(R.drawable.ic_library_books_black_24dp)
        }
    }
}