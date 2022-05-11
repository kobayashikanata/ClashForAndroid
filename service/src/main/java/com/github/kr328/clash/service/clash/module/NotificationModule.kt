package com.github.kr328.clash.service.clash.module

import android.app.Service
import android.content.Intent
import android.os.PowerManager
import androidx.core.content.getSystemService
import com.github.kr328.clash.common.constants.Intents
import com.github.kr328.clash.common.util.ticker
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.util.trafficDownload
import com.github.kr328.clash.core.util.trafficUpload
import com.github.kr328.clash.service.StatusProvider
import com.github.kr328.clash.service.expose.IStateUpdater
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeUnit

class NotificationModule(service: Service) : Module<Unit>(service) {
    companion object {
        var provider : IStateUpdater? = null
        private var notification: IStateUpdater.Notification? = null

        fun onServiceCreated(service: Service) {
            notification = provider?.create(service)
            notification?.onServiceCreated(service)
        }
    }

    override suspend fun run() = coroutineScope {
        var shouldUpdate = service.getSystemService<PowerManager>()?.isInteractive ?: true

        val screenToggle = receiveBroadcast(false, Channel.CONFLATED) {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }

        val profileLoaded = receiveBroadcast(capacity = Channel.CONFLATED) {
            addAction(Intents.ACTION_PROFILE_LOADED)
        }

        val ticker = ticker(TimeUnit.SECONDS.toMillis(1))

        val notification = NotificationModule.notification ?: return@coroutineScope
        while (true) {
            select<Unit> {
                screenToggle.onReceive {
                    when (it.action) {
                        Intent.ACTION_SCREEN_ON ->
                            shouldUpdate = true
                        Intent.ACTION_SCREEN_OFF ->
                            shouldUpdate = false
                    }
                }
                profileLoaded.onReceive {
                    notification.updateProfile(service, StatusProvider.currentUUID, StatusProvider.currentProfile)
                }
                if (shouldUpdate) {
                    ticker.onReceive {
                        update(notification)
                    }
                }
            }
        }
    }

    private fun update(notification:IStateUpdater.Notification) {
        val now = Clash.queryTrafficNow()
        val total = Clash.queryTrafficTotal()

        val uploading = now.trafficUpload()
        val downloading = now.trafficDownload()
        val uploaded = total.trafficUpload()
        val downloaded = total.trafficDownload()

        notification.updateNow(service, downloading, uploading)
        notification.updateTotal(service, downloaded, uploaded)
        notification.flushChanges(service)
    }

}