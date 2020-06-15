package robin.scaffold.jet.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.*

fun String.utc2Local(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(toLong())
    return simpleDateFormat.format(date)
}

fun String.saveToFile(fileName : String): File? {
    val root = Environment.getExternalStorageDirectory().path
    val survey = File("$root/survey")
    if (!survey.exists()) {
        survey.mkdir()
    }

    val path =
        Environment.getExternalStorageDirectory().path + "/robin/$fileName"
    val file = File(path)
    var raf: RandomAccessFile? = null
    try {
        if (!file.exists()) {
            file.createNewFile()
        }
        raf = RandomAccessFile(file, "rwd")

        raf.seek(file.length())
        raf.write(toByteArray())
        return file
    } catch (e: IOException) {
        Log.e("slog", "error saving log", e)
        return null
    } finally {
        try {
           // Log.i("slog", " file close ")
            raf?.close()
        } catch (e: IOException) {
            Log.e("slog", "error closing log", e)
        }
    }
}