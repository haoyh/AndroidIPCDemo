package com.hyh.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.hyh.ipc.R

// 客户端
class BookManagerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "BookManagerActivity"
        const val MESSAGE_NEW_BOOK_ARRIVED = 1
    }

    private var mRemoteBookManager: IBookManager? = null

    private val mHandler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MESSAGE_NEW_BOOK_ARRIVED -> {
                    Log.d(TAG, "receive new book : ${msg.obj}")
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }


    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bookManager = IBookManager.Stub.asInterface(service)
            Log.d(TAG, "bookManager: $bookManager")
            try {
                // 赋值
                mRemoteBookManager = bookManager
                val list = bookManager.bookList
                Log.d(TAG, "list type : ${list.javaClass.canonicalName}")
                Log.d(TAG, "query book list: $list")
                bookManager.addBook(Book(3, "C"))
                val newList = bookManager.bookList
                Log.d(TAG, "query book newList: $newList")
                // 注册事件
                bookManager.registerListener(mIOnNewBookArriveListener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mRemoteBookManager = null
        }
    }

    private val mIOnNewBookArriveListener = object : IOnNewBookArriveListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager)
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        Log.d(TAG, "BookManagerActivity onDestroy()")
        mRemoteBookManager?.takeIf {
            it.asBinder().isBinderAlive
        }?.let {
            try {
                Log.d(TAG, "unregister listener $mIOnNewBookArriveListener")
                // 解绑事件
                it.unregisterListener(mIOnNewBookArriveListener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        unbindService(mConnection)
        super.onDestroy()
    }
}