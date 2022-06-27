package com.github.kr328.clash.service.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.kr328.clash.common.Global
import com.github.kr328.clash.service.data.migrations.LEGACY_MIGRATION
import com.github.kr328.clash.service.data.migrations.MIGRATIONS
import com.github.kr328.clash.service.expose.globalInitConfirm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.SoftReference
import androidx.room.Database as DB

@DB(
    version = 1,
    entities = [Imported::class, Pending::class, Selection::class],
    exportSchema = false,
)
abstract class Database : RoomDatabase() {
    abstract fun openImportedDao(): ImportedDao
    abstract fun openPendingDao(): PendingDao
    abstract fun openSelectionProxyDao(): SelectionDao

    companion object {
        private var softDatabase: SoftReference<Database?> = SoftReference(null)

        @Synchronized
        private fun get(context: Context?) : Database{
            return softDatabase.get() ?: open(context ?: Global.application).apply {
                softDatabase = SoftReference(this)
            }
        }

        fun ImportedDao(context: Context): ImportedDao {
            return get(context).openImportedDao()
        }

        fun PendingDao(context: Context): PendingDao {
            return get(context).openPendingDao()
        }

        fun SelectionDao(context: Context): SelectionDao {
            return get(context).openSelectionProxyDao()
        }

        private fun open(context: Context): Database {
            globalInitConfirm(context)
            Global.launch(Dispatchers.IO) {
                LEGACY_MIGRATION(Global.application)
            }
            return Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "profiles"
            ).addMigrations(*MIGRATIONS).build()
        }
    }
}