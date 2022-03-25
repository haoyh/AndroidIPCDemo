package com.hyh.ipc.aidl

import android.os.Parcel
import android.os.Parcelable

/**
 * FileName: Book
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-22 16:55
 */
class Book() : Parcelable {

    var bookId: Int = 0
    var bookName: String = ""

    constructor(id: Int, name: String) : this() {
        this.bookId = id
        this.bookName = name
    }

    constructor(parcel: Parcel) : this() {
        bookId = parcel.readInt()
        bookName = parcel.readString() ?: ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeInt(bookId)
            writeString(bookName)
        }

    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "[bookId: $bookId, bookName: $bookName]"
    }
}