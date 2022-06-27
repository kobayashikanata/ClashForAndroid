package com.github.kr328.clash.service.expose

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.service.RemoteService
import com.github.kr328.clash.service.remote.IRemoteService
import com.github.kr328.clash.service.remote.unwrap
import java.util.concurrent.TimeUnit

open class RemoteServiceConnBase : ServiceConnection {
    protected val TOGGLE_CRASHED_INTERVAL = TimeUnit.SECONDS.toMillis(10)

    protected var lastCrashed: Long = -1
    protected var remote : IRemoteService? = null

    protected open fun target(context: Context) = RemoteService::class.intent(context)
    protected open fun onPrepare(intent: Intent) {}
    protected open fun onAttach(service : IRemoteService?){}
    protected open fun onDetach(){}

    public fun bind(context: Context):Boolean{
        unbind(context)
        return try{
            val intent = target(context)
            onPrepare(intent)
            context.bindService(intent, this, Context.BIND_AUTO_CREATE)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    public fun unbind(context: Context){
        try{
            context.unbindService(this)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder) {
        remote = service.unwrap(IRemoteService::class)
        onAttach(remote)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        remote = null
        if (System.currentTimeMillis() - lastCrashed < TOGGLE_CRASHED_INTERVAL) {
            Log.e("RemoteManager crashed")
        }
        lastCrashed = System.currentTimeMillis()
        Log.w("RemoteManager crashed")
        onDetach()
    }
}