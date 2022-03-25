package com.hyh.ipc

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log

/**
 * FileName: MyApplication
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-22 15:57
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val processName = currentProcessName(Process.myPid())
        Log.d(MainActivity.TAG, "application name $processName")
    }

    private fun Context.currentProcessName(pid: Int): String {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses?.forEach { info ->
            if (info.pid == pid) {
                return info.processName
            }
        }
        return ""
    }
}