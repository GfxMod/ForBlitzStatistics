package ru.forblitz.statistics.utils

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ZipUtils {
    companion object {

        fun packDir(inputDir: File, outputZipFile: File): File {
            val fos = FileOutputStream(outputZipFile)
            ZipArchiveOutputStream(fos).use { zos -> addDirectoryToZip(inputDir, inputDir, zos) }
            return outputZipFile
        }

        private fun addDirectoryToZip(
            rootDir: File,
            currentDir: File,
            zos: ZipArchiveOutputStream,
        ) {
            val files = currentDir.listFiles()
            if (files != null) {
                val buffer = ByteArray(1024)
                for (file in files) {
                    if (file.isDirectory) {
                        addDirectoryToZip(rootDir, file, zos)
                    } else {
                        val entryName = file.absolutePath.substring(rootDir.absolutePath.length + 1)
                        val entry = zos.createArchiveEntry(file, entryName)
                        zos.putArchiveEntry(entry)
                        FileInputStream(file).use { fis ->
                            var len: Int
                            while (fis.read(buffer).also { len = it } > 0) {
                                zos.write(buffer, 0, len)
                            }
                        }
                        zos.closeArchiveEntry()
                    }
                }
            }
        }

        fun unpackDir(zipFile: File, outputDir: File): File {
            ZipFile(zipFile).use { zip ->
                val entries = zip.entries
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val entryFile = File(outputDir, entry.name)
                    if (entry.isDirectory) {
                        entryFile.mkdirs()
                    } else {
                        entryFile.parentFile?.mkdirs()
                        zip.getInputStream(entry).use { input ->
                            BufferedOutputStream(FileOutputStream(entryFile)).use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            return outputDir
        }
    }

}