package com.hyh.ipc.contentprovider

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hyh.ipc.R
import com.hyh.ipc.aidl.Book

class ProviderActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ProviderActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)

        // 值为 authorities 的值
        val bookUri = BookProvider.BOOK_CONTENT_URI
        val values = ContentValues()
        values.put("name", "AAA-BB")
        contentResolver.insert(bookUri, values)
        contentResolver.query(bookUri, arrayOf("_id", "name"), null, null, null)?.let { cursor ->
            while (cursor.moveToNext()) {
                val book = Book(cursor.getInt(0), cursor.getString(1))
                Log.d(TAG, "book = $book")
            }
            cursor.close()
        }
    }
}