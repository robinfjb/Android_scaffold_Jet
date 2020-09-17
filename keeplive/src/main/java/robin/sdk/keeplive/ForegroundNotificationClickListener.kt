package robin.sdk.keeplive

import android.content.Context
import android.content.Intent


interface ForegroundNotificationClickListener {
    fun foregroundNotificationClick(context: Context, intent: Intent)
}