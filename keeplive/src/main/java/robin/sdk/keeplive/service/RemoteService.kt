package robin.sdk.keeplive.service

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.RemoteException
import com.fanjun.keeplive.service.GuardAidl
import robin.sdk.keeplive.NotificationUtils
import robin.sdk.keeplive.Util.isRunningTaskExist
import robin.sdk.keeplive.receiver.NotificationClickReceiver


class RemoteService : Service(){
    private lateinit var mBilder: MyBilder
    private var mIsBoundLocalService = false

    override fun onCreate() {
        super.onCreate()
        if (mBilder == null) {
            mBilder = MyBilder()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBilder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            mIsBoundLocalService = this.bindService(Intent(this@RemoteService, LocalService::class.java),
                    connection!!, Context.BIND_ABOVE_CLIENT)
        } catch (e: Exception) {
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connection != null) {
            try {
                if (mIsBoundLocalService) {
                    unbindService(connection)
                }
            } catch (e: Exception) {
            }
        }
    }

    private inner class MyBilder : GuardAidl.Stub() {
        @Throws(RemoteException::class)
        override fun wakeUp(title: String, discription: String, iconRes: Int) {
            if (Build.VERSION.SDK_INT < 25) {
                val intent2 = Intent(this@RemoteService, NotificationClickReceiver::class.java)
                intent2.action = NotificationClickReceiver.CLICK_NOTIFICATION
                val notification: Notification = NotificationUtils.createNotification(this@RemoteService, title, discription, iconRes, intent2)
                this@RemoteService.startForeground(13691, notification)
            }
        }
    }

    private val connection: ServiceConnection? = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            if (isRunningTaskExist(applicationContext, "$packageName:remote")) {
                val localService = Intent(this@RemoteService, LocalService::class.java)
                startService(localService)
                mIsBoundLocalService = this@RemoteService.bindService(Intent(this@RemoteService,
                        LocalService::class.java), this, android.content.Context.BIND_ABOVE_CLIENT)
            }
            val pm = this@RemoteService.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn = pm.isScreenOn
            if (isScreenOn) {
                sendBroadcast(Intent("_ACTION_SCREEN_ON"))
            } else {
                sendBroadcast(Intent("_ACTION_SCREEN_OFF"))
            }
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {}
    }
}