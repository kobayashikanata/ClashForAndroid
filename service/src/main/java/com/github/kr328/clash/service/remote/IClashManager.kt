package com.github.kr328.clash.service.remote

import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.model.*
import com.github.kr328.kaidl.BinderInterface

@BinderInterface
interface IClashManager {
    fun queryTunnelState(): TunnelState
    fun queryTrafficTotal(): Long
    fun queryProxyGroupNames(excludeNotSelectable: Boolean): List<String>
    fun queryProxyGroup(name: String, proxySort: ProxySort): ProxyGroup
    fun queryConfiguration(): UiConfiguration
    fun queryProviders(): ProviderList

    fun patchSelector(group: String, name: String): Boolean

    suspend fun healthCheck(group: String)
    suspend fun updateProvider(type: Provider.Type, name: String)

    fun queryOverride(slot: Clash.OverrideSlot): ConfigurationOverride
    fun patchOverride(slot: Clash.OverrideSlot, configuration: ConfigurationOverride)
    fun clearOverride(slot: Clash.OverrideSlot)

    fun setLogObserver(observer: ILogObserver?)

    fun extHealthCheckAll()
    fun extHealthCheckBlocking(name: String)
    fun patchAddSessionBypassIp(ip:String)
    fun nativeTcpTestCancel(tag:Int)
    fun nativeTcpTest(host:String, timeout:Int, maxCount:Int, tag:Int, send64Bytes:Boolean, callback: StringCallback)
    fun nativeTcpPing(host:String, pingCount:Int, timeout:Int, interval:Int, groupCount:Int, checkAlive:Boolean):String?
    fun nativeUdpPing(addr:String, count:Int, timeout:Int, packetLength:Int):String?

    @BinderInterface
    interface StringCallback{
        fun call(value:String?)
    }
}