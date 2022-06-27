package com.github.kr328.clash.service.document

import android.content.Context
import android.provider.DocumentsContract
import com.github.kr328.clash.service.R
import com.github.kr328.clash.service.data.Database
import com.github.kr328.clash.service.data.Pending
import com.github.kr328.clash.service.model.Profile
import com.github.kr328.clash.service.util.importedDir
import com.github.kr328.clash.service.util.pendingDir
import java.io.FileNotFoundException
import java.util.*

class Picker(private val context: Context) {
    suspend fun list(path: Path): List<Document> {
        if (path.uuid == null) {
            return Database.ImportedDao(context).queryAllUUIDs().map {
                pick(path.copy(uuid = it), false)
            }
        }

        if (path.scope == null) {
            return listOf(Path.Scope.Configuration, Path.Scope.Providers).map {
                pick(path.copy(scope = it), false)
            }
        }

        val parent = pick(path, false)

        if (parent !is FileDocument)
            return emptyList()

        return (parent.file.list() ?: emptyArray()).map {
            pick(path.copy(relative = (path.relative ?: emptyList()) + it), false)
        }
    }

    suspend fun pick(path: Path, writable: Boolean): Document {
        if (path.uuid == null) {
            return VirtualDocument(
                "",
                context.getString(R.string.clash_for_android),
                DocumentsContract.Document.MIME_TYPE_DIR,
                0,
                0,
                setOf(Flag.Virtual),
            )
        }

        if (writable) {
            cloneToPending(path.uuid)
        }

        val imported = Database.ImportedDao(context).queryByUUID(path.uuid)
        val pending = Database.PendingDao(context).queryByUUID(path.uuid)

        if (path.scope == null) {
            if (writable)
                throw IllegalArgumentException("invalid open mode")

            return VirtualDocument(
                id = path.uuid.toString(),
                name = pending?.name ?: imported?.name
                ?: throw FileNotFoundException("profile not found"),
                mimeType = DocumentsContract.Document.MIME_TYPE_DIR,
                size = 0,
                updatedAt = 0,
                flags = setOf(Flag.Virtual),
            )
        }

        if (path.relative == null) {
            if (path.scope == Path.Scope.Configuration) {
                val type = pending?.type ?: imported?.type
                ?: throw FileNotFoundException("profile not found")

                if (writable && type != Profile.Type.File)
                    throw IllegalArgumentException("invalid open mode")

                val flags: Set<Flag> = if (type == Profile.Type.Url)
                    emptySet()
                else
                    setOf(Flag.Writable)

                return FileDocument(
                    file = when {
                        pending != null -> context.pendingDir.resolve(pending.uuid.toString())
                        imported != null -> context.importedDir.resolve(imported.uuid.toString())
                        else -> throw FileNotFoundException("profile not found")
                    }.resolve("config.yaml"),
                    flags = flags,
                    idOverride = Paths.CONFIGURATION_ID,
                    nameOverride = context.getString(R.string.configuration_yaml)
                )
            } else {
                return FileDocument(
                    file = when {
                        pending != null -> context.pendingDir.resolve(pending.uuid.toString())
                        imported != null -> context.importedDir.resolve(imported.uuid.toString())
                        else -> throw FileNotFoundException("profile not found")
                    }.resolve("providers"),
                    idOverride = Paths.PROVIDERS_ID,
                    nameOverride = context.getString(R.string.provider_files),
                    flags = setOf(Flag.Virtual)
                )
            }
        }

        if (path.scope != Path.Scope.Providers)
            throw FileNotFoundException("invalid path")

        return FileDocument(
            file = when {
                pending != null -> context.pendingDir.resolve(pending.uuid.toString())
                imported != null -> context.importedDir.resolve(imported.uuid.toString())
                else -> throw FileNotFoundException("profile not found")
            }.resolve("providers").resolve(path.relative.joinToString(separator = "/")),
            flags = setOf(Flag.Writable, Flag.Deletable)
        )
    }

    private suspend fun cloneToPending(uuid: UUID) {
        if (Database.PendingDao(context).queryByUUID(uuid) != null)
            return

        val imported =
            Database.ImportedDao(context).queryByUUID(uuid) ?: throw FileNotFoundException("profile not found")

        Database.PendingDao(context).insert(
            Pending(
                imported.uuid,
                imported.name,
                imported.type,
                imported.source,
                imported.interval
            )
        )

        val source = context.importedDir.resolve(uuid.toString())
        val target = context.pendingDir.resolve(uuid.toString())

        target.deleteRecursively()
        source.copyRecursively(target)
    }
}