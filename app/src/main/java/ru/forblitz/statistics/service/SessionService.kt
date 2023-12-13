package ru.forblitz.statistics.service

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
import ru.forblitz.statistics.helpers.ActivityResultActionManager
import ru.forblitz.statistics.utils.ZipUtils
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * The [SessionService] class handles session-related operations.
 *
 * @property context The context for the session service.
 * @property list The list of sessions files
 */
class SessionService(private var context: Context) {
    
    private lateinit var accountId: String
    private lateinit var lastBattleTime: String
    private lateinit var json: String
    private lateinit var region: String

    lateinit var list: ArrayList<File>
    val subList: ArrayList<File>
        get() = if (list.isNotEmpty()) {
            ArrayList(list.subList(1, list.size))
        } else {
            list
        }

    var selectedSessionIndex: Int = 0

    companion object {
        private const val TMP_FILE_NAME = "tmp.fbss"

        private const val TMP_DIR_NAME = "tmp_sessions"

        private val externalDirectory = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "ForBlitzStatistics")

        private val pickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, externalDirectory)
        }
    }

    private fun sessionsDirectory(): File { return File(context.filesDir, "sessions") }

    fun exportFBSS() {
        if (!externalDirectory.exists()) {
            externalDirectory.mkdir()
        }

        ZipUtils.packDir(
            sessionsDirectory(),
            File(externalDirectory, "sessions-${System.currentTimeMillis()}.fbss")
        )
    }

    private fun importFBSS(fbssFile: File) {
        val tmpDir = File(sessionsDirectory(), TMP_DIR_NAME)
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
                file.toPath(), File(sessionsDirectory(), file.name).toPath(), StandardCopyOption.REPLACE_EXISTING
            )
        }
        tmpDir.delete()
    }

    fun importSessionsWithPicker(
        activityResultActionManager: ActivityResultActionManager,
        activityResultLauncher: ActivityResultLauncher<Intent>,
        restartActivity: () -> Unit
    ) {
        activityResultActionManager.action = { activityResult ->
            activityResult.data.also { pickedFile ->
                if (pickedFile != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            File(sessionsDirectory(), TMP_FILE_NAME).apply tmpFBSS@ {
                                outputStream().use { fileOutputStream ->
                                    context.contentResolver.openInputStream(pickedFile)!!.use { input ->
                                        input.copyTo(fileOutputStream)
                                    }
                                }
                                importFBSS(this@tmpFBSS)
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

    fun createSessionDir() {
        sessionsDirectory().mkdir()
    }

    fun createSessionFile() {
        with(File(sessionsDirectory(), "$accountId-$lastBattleTime.$region")) {
            if (!this.exists()) {
                createNewFile()
                FileWriter(path).use {
                    it.write(json)
                }
            }
        }
        update(accountId, lastBattleTime, json, region)
    }

    private fun getPlayerSessions(): ArrayList<File> {
        return ArrayList<File>().apply {
            sessionsDirectory().listFiles()?.forEach {
                if (it.isFile && it.name.substringBefore("-") == accountId && it.extension == region) {
                    this.add(it)
                }
            }
            this.sortBy { it.path }
            this.reverse()
        }
    }

    fun update(accountId: String, lastBattleTime: String, json: String,  region: String) {
        this.accountId = accountId
        this.lastBattleTime = lastBattleTime
        this.json = json
        this.region = region
        this.list = getPlayerSessions()
    }

}