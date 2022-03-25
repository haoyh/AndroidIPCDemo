// IBookManager.aidl
package com.hyh.ipc.aidl;

import com.hyh.ipc.aidl.Book;
import com.hyh.ipc.aidl.IOnNewBookArriveListener;

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(IOnNewBookArriveListener listener);

    void unregisterListener(IOnNewBookArriveListener listener);
}