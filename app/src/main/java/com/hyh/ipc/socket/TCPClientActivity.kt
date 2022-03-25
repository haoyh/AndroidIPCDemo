package com.hyh.ipc.socket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.hyh.ipc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception
import java.net.Socket

class TCPClientActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TCPClientActivity"
        const val MSG_RECEIVE_NEW_MSG = 1
        const val MSG_SOCKET_CONNECTION = 2
    }

    private lateinit var mPrintWriter: PrintWriter
    private lateinit var mClientSocket: Socket

    private lateinit var mEtInput: EditText
    private lateinit var mTvShow: TextView
    private lateinit var mBtnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tcpclient)

        mEtInput = findViewById(R.id.msg)
        mTvShow = findViewById(R.id.msg_container)

        val intent = Intent(this, TCPServerService::class.java)
        startService(intent)
        GlobalScope.launch(Dispatchers.IO) {
            // 延时一会，否则会出现首次链接失败问题
            Thread.sleep(1000)
            connectTCPServer()
        }

        mBtnSend = findViewById(R.id.send)

        mBtnSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val msg = mEtInput.text.toString().trim()
                mPrintWriter.println(msg)
                withContext(Dispatchers.Main) {
                    mTvShow.text = "${mTvShow.text}\n client: $msg"
                }
                mEtInput.setText("")
            }
        }

    }

    private suspend fun connectTCPServer() {
        var socket: Socket? = null
        while (null == socket) {
            try {
                socket = Socket("127.0.0.1", 8688)
                // 该写法也可以
//                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter =
                    PrintWriter(BufferedWriter(OutputStreamWriter(mClientSocket.getOutputStream())),
                        true)
                withContext(Dispatchers.Main) {
                    mBtnSend.isEnabled = true
                }
                Log.d(TAG, "connected")
            } catch (e: Exception) {
                Thread.sleep(1000)
                e.printStackTrace()
            }
        }

        try {
            val br = BufferedReader(InputStreamReader(socket.getInputStream()))
            while (!isFinishing) {
                val msg = br.readLine()
                Log.d(TAG, "receive $msg")
                withContext(Dispatchers.Main) {
                    mTvShow.text = "${mTvShow.text}\nserver: $msg"
                }
            }
            Log.d(TAG, "quit...")
            mPrintWriter.close()
            br.close()
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        if (!mClientSocket.isClosed) {
            mClientSocket.shutdownInput()
            mClientSocket.close()
        }
        super.onDestroy()
    }
}