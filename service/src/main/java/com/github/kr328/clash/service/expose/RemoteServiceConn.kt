package com.github.kr328.clash.service.expose

import android.content.Intent
import com.github.kr328.clash.service.remote.IRemoteService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RemoteServiceConn : RemoteServiceConnBase() {
    private var latch = CountDownLatch(1)

    fun getRemoteBlocking(timeout:Long) : IRemoteService {
        if(remote != null) return remote!!
        latch.await(timeout, TimeUnit.MILLISECONDS);
        return remote!!
    }

    override fun onPrepare(intent: Intent) {
        super.onPrepare(intent)
        ensureLatch()
    }

    override fun onAttach(service: IRemoteService?) {
        super.onAttach(service)
        latch.countDown()
    }

    override fun onDetach() {
        super.onDetach()
        ensureLatch()
    }

    private fun ensureLatch(){
        if(latch.count <= 0){
            latch = CountDownLatch(1)
        }
    }
}