package com.hyh.ipc.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

/**
 * FileName: BookProvider
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-23 18:09
 */
class BookProvider : ContentProvider() {

    companion object {
        const val TAG = "BookProvider"

        const val AUTHORITY = "com.hyh.book.provider"

        val BOOK_CONTENT_URI = Uri.parse("content://$AUTHORITY/book")
        val USER_CONTENT_URI = Uri.parse("content://$AUTHORITY/user")

        const val BOOK_URI_CODE = 0
        const val USER_URI_CODE = 1

        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        // 关联 code 和 uri
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE)
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE)
    }


    private lateinit var mDb: SQLiteDatabase

    fun getTableName(uri: Uri): String {
        return when (sUriMatcher.match(uri)) {
            BOOK_URI_CODE -> {
                DbOpenHelper.BOOK_TABLE_NAME
            }
            USER_URI_CODE -> {
                DbOpenHelper.USER_TABLE_NAME
            }
            else -> {
                ""
            }
        }
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate current Thread ${Thread.currentThread().name}")
        context?.let {
            mDb = DbOpenHelper(it).writableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        Log.d(TAG, "query current Thread ${Thread.currentThread().name}")
        val table = getTableName(uri)
        table?.let {
            return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null)
        }
        return null
    }

    override fun getType(uri: Uri): String? {
        Log.d(TAG, "getType current Thread ${Thread.currentThread().name}")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert current Thread ${Thread.currentThread().name}")
        val table =  getTableName(uri)
        mDb.insert(table, null, values)
        // 通知更新
        context?.contentResolver?.notifyChange(uri, null)
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete current Thread ${Thread.currentThread().name}")
        getTableName(uri).let {
            val count = mDb.delete(it, selection, selectionArgs)
            if (count > 0) {
                context?.contentResolver?.notifyChange(uri, null)
            }
            return count
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        Log.d(TAG, "update current Thread ${Thread.currentThread().name}")
        getTableName(uri).let {
            val row = mDb.update(it, values, selection, selectionArgs)
            if (row > 0) {
                context?.contentResolver?.notifyChange(uri, null)
            }
            return row
        }
    }
}