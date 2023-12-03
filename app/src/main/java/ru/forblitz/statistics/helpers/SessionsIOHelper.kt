package ru.forblitz.statistics.helpers

import android.content.Context
import android.os.Environment
import ru.forblitz.statistics.utils.ZipUtils
import java.io.File


class SessionsIOHelper {
    companion object {

        private val externalDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ForBlitzStatistics")

        private fun sessionsDirectory(context: Context): File { return File(context.filesDir, "sessions") }

        fun exportSessions(context: Context) {
            if (!externalDirectory.exists()) {
                externalDirectory.mkdir()
            }

            ZipUtils.packDir(
                sessionsDirectory(context),
                File(externalDirectory, "sessions-${System.currentTimeMillis()}.fbss")
            )
        }

        fun importSessions(context: Context, sessionsFile: File) {
            ZipUtils.unpackDir(
                sessionsFile,
                sessionsDirectory(context)
            )
        }

    }
}