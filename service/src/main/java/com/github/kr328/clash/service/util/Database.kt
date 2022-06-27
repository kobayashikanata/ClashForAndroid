package com.github.kr328.clash.service.util

import android.content.Context
import com.github.kr328.clash.service.data.Database
import java.util.*

suspend fun generateProfileUUID(context: Context): UUID {
    var result = UUID.randomUUID()

    while (Database.ImportedDao(context).exists(result) ||Database. PendingDao(context).exists(result)) {
        result = UUID.randomUUID()
    }

    return result
}
