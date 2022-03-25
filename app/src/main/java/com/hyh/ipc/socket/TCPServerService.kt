package com.hyh.ipc.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlin.random.Random

class TCPServerService : Service() {

    companion object {
        const val TAG = "TCPServerService"
    }

    private var mIsServiceDestroy = false

    private val mDefinedMessage =
        arrayListOf("hello", "What's your name?", "hi", "nice to meet you")

    override fun onCreate() {
        Thread(TcpServer()).start()
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        mIsServiceDestroy = true
        super.onDestroy()
    }

    inner class TcpServer : Runnable {
        override fun run() {
            var serverSocket: ServerSocket? = null
            try {
                // 监听 8688 端口
                serverSocket = ServerSocket(8688)
                Log.d(TAG, "power")
            } catch (e: IOException) {
                Log.d(TAG, "establish tcp server failed, port: 8688")
                e.printStackTrace()
                return
            }

            while (!mIsServiceDestroy) {
                try {
                    // 接收客户端请求
                    val client = serverSocket.accept()
                    Log.d(TAG, "accept")
                    Thread {
                        responseClient(client)
                    }.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun responseClient(client: Socket) {
        // 接收客户端信息
        val ir = BufferedReader(InputStreamReader(client.getInputStream()))
        // 给客户端发消息
        val out = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
        out.println("welcome~")
        while (!mIsServiceDestroy) {
            val str = ir.readLine()
            Log.d(TAG, "msg from client $str")
            if (null == str) {
                // 断开客户端连接
                break
            }
            val i = Random.nextInt(0, mDefinedMessage.size - 1)
            val msg = mDefinedMessage[i]
            out.println(msg)
            Log.d(TAG, "msg send to client $msg")
        }
        Log.d(TAG, "client quit")
        out.close()
        ir.close()
        client.close()
    }
}