package com.hyh.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * FileName: BookManagerService
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-23 16:17
 */
// 服务端
class BookManagerService : Service() {

    companion object {
        const val TAG = "BMS"
    }

    private var mIsServiceDestroyed = AtomicBoolean(false)

    val mBookList = CopyOnWriteArrayList<Book>()

    private val mListenerList = RemoteCallbackList<IOnNewBookArriveListener>()

    private val mBinder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            return mBookList
        }

        override fun addBook(book: Book?) {
            mBookList.add(book)
        }

        override fun registerListener(listener: IOnNewBookArriveListener?) {
            mListenerList.register(listener)
            val n = mListenerList.beginBroadcast()
            Log.d(TAG, "registerListener  $n")
            mListenerList.finishBroadcast()
        }

        override fun unregisterListener(listener: IOnNewBookArriveListener?) {
            val success = mListenerList.unregister(listener)
            if (success) {
                Log.d(TAG, "unregister success.")
            } else {
                Log.d(TAG, "not found, can not unregister.")
            }
            // beginBroadcast() 和 finishBroadcast() 需要配对使用
            val N = mListenerList.beginBroadcast()
            mListenerList.finishBroadcast()
            Log.d(TAG, "unregisterListener, current size:$N")
        }
    }

    override fun onCreate() {
        super.onCreate()
        mBookList.add(Book(1, "A"))
        mBookList.add(Book(1, "B"))
        Thread(ServiceWorker()).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsServiceDestroyed.set(true)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    private fun onNewBookArrived(book: Book) {
        mBookList.add(book)
        val N = mListenerList.beginBroadcast()
        for (i in 0 until N) {
            val l = mListenerList.getBroadcastItem(i)
            l?.let {
                l.onNewBookArrived(book)
            }
        }
        mListenerList.finishBroadcast()
    }

    inner class ServiceWorker : Runnable {

        override fun run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val bookId = mBookList.size + 1
                val newBook = Book(bookId, "new book#$bookId")
                try {
                    onNewBookArrived(newBook)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

    }
}