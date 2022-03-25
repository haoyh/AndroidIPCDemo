package com.hyh.ipc

import android.os.Parcel
import android.os.Parcelable

/**
 * FileName: User
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-22 16:20
 */
class User() : Parcelable {

    var id: Int = 0
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}