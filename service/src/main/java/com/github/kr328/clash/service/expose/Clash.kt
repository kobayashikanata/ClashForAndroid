package com.github.kr328.clash.service.expose

import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.kr328.clash.common.compat.startForegroundServiceCompat
import com.github.kr328.clash.common.constants.Intents
import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.service.ClashService
import com.github.kr328.clash.service.TunService
import com.github.kr328.clash.service.data.Imported
import com.github.kr328.clash.service.data.ImportedDao
import com.github.kr328.clash.service.model.Profile
import com.github.kr328.clash.service.util.importedDir
import com.github.kr328.clash.service.util.sendBroadcastSelf
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

fun queryTrafficNow() = Clash.queryTrafficNow()
fun queryTrafficTotal() = Clash.queryTrafficTotal()

fun stopClash(context:Context){
    context.sendBroadcastSelf(Intent(Intents.ACTION_CLASH_REQUEST_STOP))
}

fun startClash(context: Context, startTun:Boolean) =
    context.startForegroundServiceCompat(intent(context, startTun))

fun startClashForeground(context: Context, startTun:Boolean) =
    context.startService(intent(context, startTun))

private fun intent(context: Context, startTun:Boolean):Intent{
    return if(startTun) TunService::class.intent
    else ClashService::class.intent
}

fun writeConfig(context: Context, uuid: UUID, name:String, text: String){
    getConfigFile(context, uuid).writeText(text, StandardCharsets.UTF_8)
    importProfile(uuid, name)
}

fun getConfigFile(context:Context, uuid: UUID): File {
    val file = context.importedDir.resolve(uuid.toString()).resolve("config.yaml")
    if(!file.parentFile!!.exists()){
        file.parentFile!!.mkdirs()
    }
    if(!file.exists()){
        file.createNewFile()
    }
    return file
}

fun importProfile(uuid:UUID, name:String){
    val model = Imported(
        uuid, name,
        Profile.Type.File, "", 0L,
        System.currentTimeMillis()
    )
    runBlocking {
        val dao = ImportedDao()
        if(!dao.exists(model.uuid)){
            dao.insert(model)
        }else{
            dao.update(model)
        }
    }
}

fun Context.startForegroundServiceCompat(intent: Intent) {
    if (Build.VERSION.SDK_INT >= 26) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}