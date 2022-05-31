package com.github.kr328.clash.service.clash.module

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageInfo
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.core.Clash
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class AppListCacheModule(service: Service) : Module<Unit>(service) {
    private fun PackageInfo.uniqueUidName(): String =
        if (sharedUserId != null && sharedUserId.isNotBlank()) sharedUserId else packageName

    private fun reload(){
        try{
            reloadUnsafe()
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }

//    if app use clash service not support query pkg
//    please add below to manifest
//    <uses-permission
//    android:name="android.permission.QUERY_ALL_PACKAGES"
//    tools:node="remove"
//    tools:ignore="QueryAllPackagesPermission" />
    @SuppressLint("QueryPermissionsNeeded")
    private fun reloadUnsafe() {
        val packages = service.packageManager.getInstalledPackages(0)
            .groupBy { it.uniqueUidName() }
            .map { (_, v) ->
                val info = v[0]

                if (v.size == 1) {
                    // Force use package name if only one app in a single sharedUid group
                    // Example: firefox

                    info.applicationInfo.uid to info.packageName
                } else {
                    info.applicationInfo.uid to info.uniqueUidName()
                }
            }

        Clash.notifyInstalledAppsChanged(packages)

        Log.d("Installed ${packages.size} packages cached")
    }

    override suspend fun run() {
        val packageChanged = receiveBroadcast(false, Channel.CONFLATED) {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }

        while (true) {
            reload()

            packageChanged.receive()

            delay(TimeUnit.SECONDS.toMillis(10))
        }
    }
}