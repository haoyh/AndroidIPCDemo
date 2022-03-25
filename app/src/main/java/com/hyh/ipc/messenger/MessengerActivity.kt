package com.hyh.ipc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hyh.ipc.R

class MessengerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MessengerActivity"
    }

    // 接收消息
    private val mReceiveMessenger = Messenger(MessengerHandler(Looper.myLooper()!!))

    class MessengerHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MSG_FROM_SERVICE -> {
                    // 接收服务端消息
                    Log.d(TAG, "receive msg from service: ${msg.data.getString("reply")}")
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

    // 发送消息
    private lateinit var mService: Messenger

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = Messenger(service)
            val msg = Message.obtain(null, Constants.MSG_FROM_CLIENT)
            val data = Bundle()
            data.putString("msg", "hello, this is client.")
            msg.data = data
            // 将接收服务器消息的对象传递给服务端
            msg.replyTo = mReceiveMessenger
            try {
                mService.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        // 绑定服务端 service
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }
}