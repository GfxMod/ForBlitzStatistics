package ru.forblitz.statistics.helpers

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.forblitz.statistics.R
import ru.forblitz.statistics.dto.UserStatisticsResponse
import ru.forblitz.statistics.utils.ZipUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class SessionsIOHelper {
    companion object {

        private const val TMP_FILE_NAME = "tmp.fbss"

        private const val TMP_DIR_NAME = "tmp_sessions"

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
            val tmpDir = File(sessionsDirectory(context), TMP_DIR_NAME)
            with(tmpDir) {
                if (exists()) {
                    deleteRecursively()
                }
                mkdir()
            }
            ZipUtils.unpackDir(
                fbssFile,
                tmpDir
            )
            tmpDir.listFiles()?.forEach { file ->
                // try to parse file as statistics
                // it throw exception if it not statistics
                Gson().fromJson(
                    JsonParser
                        .parseString(file.readText())
                        .asJsonObject,
                    UserStatisticsResponse::class.java)
                // we can move file to
                Files.move(
                    file.toPath(), File(sessionsDirectory(context), file.name).toPath(), StandardCopyOption.REPLACE_EXISTING
                )
            }
            tmpDir.delete()
        }

        fun importSessionsWithPicker(
            context: Context,
            activityResultActionManager: ActivityResultActionManager,
            activityResultLauncher: ActivityResultLauncher<Intent>,
            restartActivity: () -> Unit
        ) {
            activityResultActionManager.action = { activityResult ->
                activityResult.data.also { pickedFile ->
                    if (pickedFile != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                File(sessionsDirectory(context), TMP_FILE_NAME).apply tmpFBSS@ {
                                    outputStream().use { fileOutputStream ->
                                        context.contentResolver.openInputStream(pickedFile)!!.use { input ->
                                            input.copyTo(fileOutputStream)
                                        }
                                    }
                                    importFBSS(context, this@tmpFBSS)
                                    delete()
                                }
                                restartActivity.invoke()
                            } catch (e: Exception) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(context, context.getString(R.string.sessions_io_import_error, e.javaClass), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }

            activityResultLauncher.launch(pickerIntent)
        }

    }
}