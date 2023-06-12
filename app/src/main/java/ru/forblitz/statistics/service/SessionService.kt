package ru.forblitz.statistics.service

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * The [SessionService] class handles session-related operations.
 *
 * @property context The context for the session service.
 * @property list The list of sessions.
 * @property alreadySet Indicates if the session has already been set.
 */
class SessionService(private var context: Context) {

    private lateinit var list: ArrayList<String>
    var alreadySet: Boolean = false

    /**
     * Returns a list of all sessions for the [player ID][userID] in the
     * specified [region]
     * @param userID ID of the player whose sessions should be returned
     * @param region The cluster where the player is located
     */
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


    /**
     * Returns the [File] that has the largest timestamp in the name
     * @param region The cluster where the search needs to be performed
     */
    @Deprecated("Use a history database instead")
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

    /**
     * Returns the session directory
     */
    private fun getSessionDir(): File {
        return File(context.filesDir, "sessions")
    }

    /**
     * Creates a session file. The name is constructed according to the scheme:
     * "[userID]-[lastBattleTime].[region]"
     *
     * @param json json to be saved to a file
     * @param userID ID of the player whose session should be saved
     * @param lastBattleTime last battle time of the player whose session
     * should be saved
     * @param region the cluster where the player is located
     */
    fun createSessionFile(json: String, userID: String, lastBattleTime: String, region: String) {

        val file = File(getSessionDir(), "$userID-$lastBattleTime.$region")

        if (!file.exists()) {
            Files.createFile(Paths.get("$file"))
            FileWriter("$file").use { it.write(json) }
        }

    }

    /**
     * Creates the session directory to store the cache
     */
    fun createSessionDir() {
        Files.createDirectories(Paths.get(getSessionDir().toString()))
    }

    /**
     * Clears [alreadySet]
     */
    fun clear() {
        alreadySet = false
    }

}