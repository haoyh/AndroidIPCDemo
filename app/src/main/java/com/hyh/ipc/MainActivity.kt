package com.hyh.ipc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hyh.ipc.binderpool.BinderPoolActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "hyh-lifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "main onCreate")
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_open_next_activity)?.setOnClickListener {
            // 启动各个包下的 activity
            startActivity(Intent(this, BinderPoolActivity::class.java))
//            startActivity(Intent(this, BookManagerActivity::class.java))
        }
    }

}