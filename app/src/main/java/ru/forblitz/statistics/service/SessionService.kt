package ru.forblitz.statistics.service

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SessionService(private var context: Context) {

    private lateinit var list: ArrayList<String>
    var alreadySet: Boolean = false

    fun getSessionsList(userID: String, region: String): ArrayList<String> {
        list = ArrayList()

        Files.walk(Paths.get(getSessionDir().toString()))
            .filter { path: Path? ->
                Files.isRegularFile(
                    path
                )
            }
            .filter { path: Path ->
                path.toString().substring(path.toString().lastIndexOf("/") + 1, path.toString().indexOf("-")) == userID
            }
            .filter { path: Path ->
                path.toString().substring(path.toString().lastIndexOf(".") + 1) == region
            }
            .forEach { path: Path -> list.add(path.toString()) }

        list.sort()
        list.reverse()

        return list
    }

    fun getLastFile(region: String): File? {
        var lastFile: File? = null

        Files.walk(Paths.get(getSessionDir().toString()))
            .filter { path: Path? -> Files.isRegularFile(path) }
            .filter { path: Path ->
                path.toString().substring(path.toString().lastIndexOf(".") + 1) == region
            }
            .forEach { path: Path ->
                if (lastFile != null) {
                    if (lastFile!!.lastModified() < File(path.toString()).lastModified()) {
                        lastFile = File(path.toString())
                    }
                } else {
                    lastFile = File(path.toString())
                }
            }

        return lastFile
    }

    fun getSessionsList(): ArrayList<String> {
        return list
    }

    private fun getSessionDir(): File {
        return File(context.filesDir, "sessions")
    }

    fun createSessionFile(json: String, userID: String, lastBattleTime: String, region: String) {

        val file = File(getSessionDir(), "$userID-$lastBattleTime.$region")

        if (!file.exists()) {
            Files.createFile(Paths.get("$file"))
            FileWriter("$file").use { it.write(json) }
        }

    }

    fun createSessionDir() {
        Files.createDirectories(Paths.get(getSessionDir().toString()))
    }

    fun clear() {
        alreadySet = false
    }

}