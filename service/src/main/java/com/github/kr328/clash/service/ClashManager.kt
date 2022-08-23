package com.github.kr328.clash.service

import android.content.Context
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.bridge.Bridge
import com.github.kr328.clash.core.bridge.StringCallback
import com.github.kr328.clash.core.model.*
import com.github.kr328.clash.service.data.Database
import com.github.kr328.clash.service.data.Selection
import com.github.kr328.clash.service.remote.IClashManager
import com.github.kr328.clash.service.remote.ILogObserver
import com.github.kr328.clash.service.store.ServiceStore
import com.github.kr328.clash.service.util.sendOverrideChanged
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel

class ClashManager(private val context: Context) : IClashManager,
    CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val store = ServiceStore(context)
    private var logReceiver: ReceiveChannel<LogMessage>? = null

    override fun patchAddSessionBypassIp(ip: String) {
        Bridge.patchAddBypassIp(ip)
    }

    override fun nativeTcpTestCancel(tag: Int) {
        Bridge.nativeTcpTestCancel(tag)
    }

    override fun nativeTcpTest(
        host: String,
        timeout: Int,
        maxCount: Int,
        tag: Int,
        send64Bytes: Boolean,
        callback: IClashManager.StringCallback
    ) {
        Bridge.nativeTcpTest(host, timeout, maxCount, tag, send64Bytes) { value -> callback.call(value) }
    }

    override fun nativeTcpPing(
        host: String,
        pingCount: Int,
        timeout: Int,
        interval: Int,
        groupCount: Int,
        checkAlive: Boolean
    ): String? {
        return Bridge.nativeTcpPing(host, pingCount, timeout, interval, groupCount, checkAlive)
    }

    override fun nativeUdpPing(addr: String, count: Int, timeout: Int, packetLength: Int): String? {
        return Bridge.nativeUdpPing(addr, count, timeout, packetLength)
    }

    override fun extHealthCheckAll() {
        Clash.healthCheckAll()
    }

    override fun extHealthCheckBlocking(name: String) {
        runBlocking {
            Clash.healthCheck(name).await()
        }
    }

    override fun queryTunnelState(): TunnelState {
        return Clash.queryTunnelState()
    }

    override fun queryTrafficTotal(): Long {
        return Clash.queryTrafficTotal()
    }

    override fun queryProxyGroupNames(excludeNotSelectable: Boolean): List<String> {
        return Clash.queryGroupNames(excludeNotSelectable)
    }

    override fun queryProxyGroup(name: String, proxySort: ProxySort): ProxyGroup {
        return Clash.queryGroup(name, proxySort)
    }

    override fun queryConfiguration(): UiConfiguration {
        return Clash.queryConfiguration()
    }

    override fun queryProviders(): ProviderList {
        return ProviderList(Clash.queryProviders())
    }

    override fun queryOverride(slot: Clash.OverrideSlot): ConfigurationOverride {
        return Clash.queryOverride(slot)
    }

    override fun patchSelector(group: String, name: String): Boolean {
        return Clash.patchSelector(group, name).also {
            val current = store.activeProfile ?: return@also

            if (it) {
                Database.SelectionDao(context).setSelected(Selection(current, group, name))
            } else {
                Database.SelectionDao(context).removeSelected(current, group)
            }
        }
    }

    override fun patchOverride(slot: Clash.OverrideSlot, configuration: ConfigurationOverride) {
        Clash.patchOverride(slot, configuration)

        context.sendOverrideChanged()
    }

    override fun clearOverride(slot: Clash.OverrideSlot) {
        Clash.clearOverride(slot)
    }

    override suspend fun healthCheck(group: String) {
        return Clash.healthCheck(group).await()
    }

    override suspend fun updateProvider(type: Provider.Type, name: String) {
        return Clash.updateProvider(type, name).await()
    }

    override fun setLogObserver(observer: ILogObserver?) {
        synchronized(this) {
            logReceiver?.apply {
                cancel()

                Clash.forceGc()
            }

            if (observer != null) {
                logReceiver = Clash.subscribeLogcat().also { c ->
                    launch {
                        try {
                            while (isActive) {
                                observer.newItem(c.receive())
                            }
                        } catch (e: CancellationException) {
                            // intended behavior
                            // ignore
                        } catch (e: Exception) {
                            Log.w("UI crashed", e)
                        } finally {
                            withContext(NonCancellable) {
                                c.cancel()

                                Clash.forceGc()
                            }
                        }
                    }
                }
            }
        }
    }
}