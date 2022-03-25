package com.hyh.ipc.binderpool

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import java.util.concurrent.CountDownLatch

/**
 * FileName: BinderPool
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-25 15:18
 */
class BinderPool(context: Context) {

    companion object {
        const val BINDER_NONE = -1
        const val BINDER_COMPUTE = 0
        const val BINDER_SECURITY_CENTER = 1
    }

    private var mContext: Context = context.applicationContext
    private lateinit var mBinderPool: IBinderPool


    private lateinit var mConnectBinderPoolCountDownLatch: CountDownLatch
    private var mBinderPoolDeathRecipient: IBinder.DeathRecipient? = null
    private lateinit var mBinderPoolConnection: ServiceConnection

    init {

        mBinderPoolDeathRecipient = object : IBinder.DeathRecipient {
            override fun binderDied() {
                mBinderPoolDeathRecipient?.let {
                    mBinderPool.asBinder()?.unlinkToDeath(it, 0)
                }
                connectBinderPoolService()
            }
        }

        mBinderPoolConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mBinderPool = IBinderPool.Stub.asInterface(service)
                mBinderPoolDeathRecipient?.let {
                    mBinderPool.asBinder()?.linkToDeath(it, 0)
                }
                mConnectBinderPoolCountDownLatch.countDown()

            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }

        connectBinderPoolService()

    }

    @Synchronized
    private fun connectBinderPoolService() {
        Log.d("hyh", "connectBinderPoolService()")
        mConnectBinderPoolCountDownLatch = CountDownLatch(1)
        val service = Intent(mContext, BinderPoolService::class.java)
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            // todo 会造成 ANR
//            mConnectBinderPoolCountDownLatch.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun queryBinder(binderCode: Int): IBinder? {
        return mBinderPool.queryBinder(binderCode)
    }

    val a = object : ISecurityCenter.Stub() {
        override fun encrypt(content: String?): String {
            return "encrypt $content"
        }

        override fun decrypt(password: String?): String {
            return "decrypt $password"
        }
    }

    val b = object : ICompute.Stub() {
        override fun add(a: Int, b: Int): Int {
            return a.plus(b)
        }
    }

    inner class BinderPoolImpl : IBinderPool.Stub() {
        override fun queryBinder(binderCode: Int): IBinder? {
            Log.d("hyh", "queryBinder $binderCode")
            return when (binderCode) {
                BINDER_SECURITY_CENTER -> {
                    a
                }
                BINDER_COMPUTE -> {
                    b
                }
                else -> {
                    null
                }
            }
        }
    }
}