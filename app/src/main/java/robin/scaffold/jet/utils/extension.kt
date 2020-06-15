package robin.scaffold.jet.utils

import android.app.Activity
import android.app.Fragment
import android.app.Service
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.*


inline fun <reified T: Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
        Internals.internalStartActivity(this, T::class.java, params)

inline fun <reified T: Activity> Activity.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        Internals.internalStartActivityForResult(this, T::class.java, requestCode, params)

inline fun <reified T: Service> Context.startService(vararg params: Pair<String, Any?>) =
        Internals.internalStartService(this, T::class.java, params)


fun String.utc2Local(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(toLong())
    return simpleDateFormat.format(date)
}