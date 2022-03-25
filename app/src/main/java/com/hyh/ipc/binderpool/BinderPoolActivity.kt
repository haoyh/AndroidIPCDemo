package com.hyh.ipc.binderpool

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hyh.ipc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BinderPoolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)

        GlobalScope.launch(Dispatchers.IO) {
            work()
        }

    }

    private fun work() {
        val pool = BinderPool(this@BinderPoolActivity)
        Log.d("hyh", "11111")
        Thread.sleep(2000)
        Log.d("hyh", "2222")
        val seBinder = pool.queryBinder(BinderPool.BINDER_SECURITY_CENTER)
        Log.d("hyh", "seBinder $seBinder")
        val center = ISecurityCenter.Stub.asInterface(seBinder)
        Log.d("hyh", "center $center")
        Log.d("hyh", center.encrypt("aaa"))
        Log.d("hyh", center.decrypt("bb"))
        val com = ICompute.Stub.asInterface(pool.queryBinder(BinderPool.BINDER_COMPUTE))
        Log.d("hyh", "add() $com.add(1, 3)")
    }
}