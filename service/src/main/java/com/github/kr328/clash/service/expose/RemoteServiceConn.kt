package com.github.kr328.clash.service.expose

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.service.RemoteService
import com.github.kr328.clash.service.remote.IRemoteService
import com.github.kr328.clash.service.remote.unwrap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RemoteServiceConn : ServiceConnection {
    private val TOGGLE_CRASHED_INTERVAL = TimeUnit.SECONDS.toMillis(10)

    private var lastCrashed: Long = -1
    private var remote : IRemoteService? = null
    private val latch = CountDownLatch(1)

    fun target() = RemoteService::class.intent

    fun getRemoteBlocking(timeout:Long) : IRemoteService {
        latch.await(timeout, TimeUnit.MILLISECONDS);
        return remote!!
    }

    fun bind(context: Context):Boolean{
        unbind(context)
        return try{
            context.bindService(target(), this, Context.BIND_AUTO_CREATE)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    fun unbind(context:Context){
        try{
            context.unbindService(this)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder) {
        remote = service.unwrap(IRemoteService::class)
        latch.countDown()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        remote = null
        if (System.currentTimeMillis() - lastCrashed < TOGGLE_CRASHED_INTERVAL) {
            Log.e("RemoteManager crashed")
        }
        lastCrashed = System.currentTimeMillis()
        Log.w("RemoteManager crashed")
    }
}