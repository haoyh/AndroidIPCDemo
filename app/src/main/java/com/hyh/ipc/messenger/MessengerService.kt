package com.hyh.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.hyh.ipc.messenger.Constants.MSG_FROM_CLIENT
import com.hyh.ipc.messenger.Constants.MSG_FROM_SERVICE

class MessengerService : Service() {

    companion object {
        const val TAG = "MessengerService"
    }

    class MessengerHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FROM_CLIENT -> {
                    // 接收到客户端消息
                    Log.d(TAG, "receive msg from Client: ${msg.data.getString("msg")}")
                    // 服务器回复客户端消息
                    val client = msg.replyTo
                    val replayMsg = Message.obtain(null, MSG_FROM_SERVICE)
                    val bundle = Bundle()
                    bundle.putString("reply", "already receive, replay soon.")
                    replayMsg.data = bundle
                    try {
                        client.send(replayMsg)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

    private val mMessenger = Messenger(MessengerHandler(Looper.myLooper()!!))

    override fun onBind(intent: Intent): IBinder {
        return mMessenger.binder
    }
}