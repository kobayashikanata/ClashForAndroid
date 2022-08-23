package com.github.kr328.clash.service.expose

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.service.RemoteService
import com.github.kr328.clash.service.remote.IRemoteService
import com.github.kr328.clash.service.remote.unwrap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

open class RemoteService(private val context: Context) {
    public var remote : IRemoteService? = null
    private val latch = CountDownLatch(1)


    private val connection = object : ServiceConnection {
        private var lastCrashed: Long = -1

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            remote = service.unwrap(IRemoteService::class)
            latch.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            remote = null
            if (System.currentTimeMillis() - lastCrashed < TOGGLE_CRASHED_INTERVAL) {
                unbind()
                crashed()
            }
            lastCrashed = System.currentTimeMillis()
            Log.w("RemoteManager crashed")
        }
    }

    fun bind() {
        try {
            context.bindService(RemoteService::class.intent(context), connection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            e.printStackTrace()
            unbind()
            crashed()
        }
    }

    fun unbind() {
        try {
            context.unbindService(connection)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        remote = null
    }

    private fun crashed(){

    }

    companion object {
        private val TOGGLE_CRASHED_INTERVAL = TimeUnit.SECONDS.toMillis(10)
    }
}