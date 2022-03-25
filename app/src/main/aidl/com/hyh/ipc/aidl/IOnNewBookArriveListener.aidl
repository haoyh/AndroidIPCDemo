// IOnNewBookArriveListener.aidl
package com.hyh.ipc.aidl;
import com.hyh.ipc.aidl.Book;

interface IOnNewBookArriveListener {
    void onNewBookArrived(in Book newBook);
}