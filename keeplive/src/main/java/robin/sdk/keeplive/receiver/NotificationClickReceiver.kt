package robin.sdk.keeplive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import robin.sdk.keeplive.KeepLive


class NotificationClickReceiver : BroadcastReceiver() {
    companion object {
        val CLICK_NOTIFICATION = "CLICK_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == CLICK_NOTIFICATION) {
            if (KeepLive.foregroundNotification != null) {
                if (KeepLive.foregroundNotification.getForegroundNotificationClickListener() != null) {
                    KeepLive.foregroundNotification.getForegroundNotificationClickListener()!!.foregroundNotificationClick(context, intent)
                }
            }
        }
    }
}