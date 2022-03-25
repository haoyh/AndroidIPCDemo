package com.hyh.ipc.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * FileName: BinderPoolService
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-25 15:19
 */
class BinderPoolService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return BinderPool(applicationContext).BinderPoolImpl()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}