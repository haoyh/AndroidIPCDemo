package com.hyh.ipc.contentprovider

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * FileName: DbOpenHelper
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-23 18:29
 */
class DbOpenHelper @JvmOverloads constructor(
    val context: Context,
    name: String = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = DB_VERSION,
    errorHandler: DatabaseErrorHandler? = null,
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {

    companion object {
        const val DB_NAME = "book_provider.db"
        const val BOOK_TABLE_NAME = "book"
        const val USER_TABLE_NAME = "user"
        const val DB_VERSION = 1

        const val CREATE_BOOK_TABLE =
            "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME (_id INTEGER PRIMARY KEY, name TEXT)"

        const val CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (_id INTEGER PRIMARY KEY, name TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BOOK_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}