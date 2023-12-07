package ru.forblitz.statistics.helpers

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.DocumentsContract
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.forblitz.statistics.utils.ZipUtils
import java.io.File


class SessionsIOHelper {
    companion object {

        private const val TMP_FILE_NAME = "tmp.fbss"

        private val externalDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ForBlitzStatistics")

        private fun sessionsDirectory(context: Context): File { return File(context.filesDir, "sessions") }

        private val pickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, externalDirectory)
        }

        fun exportFBSS(context: Context) {
            if (!externalDirectory.exists()) {
                externalDirectory.mkdir()
            }

            ZipUtils.packDir(
                sessionsDirectory(context),
                File(externalDirectory, "sessions-${System.currentTimeMillis()}.fbss")
            )
        }

        private fun importFBSS(context: Context, fbssFile: File) {
            ZipUtils.unpackDir(
                fbssFile,
                sessionsDirectory(context)
            )
        }

        fun importSessionsWithPicker(
            context: Context,
            activityResultActionManager: ActivityResultActionManager,
            activityResultLauncher: ActivityResultLauncher<Intent>
        ) {
            activityResultActionManager.action = { activityResult ->
                activityResult.data.also { pickedFile ->
                    if (pickedFile != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            File(sessionsDirectory(context), TMP_FILE_NAME).apply tmpFBSS@ {
                                outputStream().use { fileOutputStream ->
                                    context.contentResolver.openInputStream(pickedFile)!!.use { input ->
                                        input.copyTo(fileOutputStream)
                                    }
                                }
                                importFBSS(context, this@tmpFBSS)
                                delete()
                            }
                        }
                    }
                }
            }

            activityResultLauncher.launch(pickerIntent)
        }

    }
}