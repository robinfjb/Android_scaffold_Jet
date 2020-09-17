package robin.sdk.keeplive.service

import android.app.Notification
import android.app.Service
import android.content.*
import android.media.MediaPlayer
import android.os.*
import com.fanjun.keeplive.service.GuardAidl
import robin.sdk.keeplive.KeepLive
import robin.sdk.keeplive.NotificationUtils
import robin.sdk.keeplive.R
import robin.sdk.keeplive.Util.isServiceRunning
import robin.sdk.keeplive.receiver.NotificationClickReceiver
import robin.sdk.keeplive.receiver.OnepxReceiver


class LocalService :Service(){
    private var mOnepxReceiver: OnepxReceiver? = null
    private var screenStateReceiver: ScreenStateReceiver? = null
    private var isPause = true //控制暂停

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var mBilder: MyBilder
    private var handler: Handler? = null
    private var mIsBoundRemoteService = false

    override fun onCreate() {
        super.onCreate()
        if (mBilder == null) {
            mBilder = MyBilder()
        }
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        isPause = pm.isScreenOn
        if (handler == null) {
            handler = Handler()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBilder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //播放无声音乐
        if (KeepLive.useSilenceMusice) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.novioce)
            }
            mediaPlayer?.setVolume(0f, 0f)
            mediaPlayer?.setOnCompletionListener {
                if (!isPause) {
                    if (KeepLive.runMode === KeepLive.RunMode.ROGUE) {
                        play()
                    } else {
                        if (handler != null) {
                            handler!!.postDelayed({ play() }, 5000)
                        }
                    }
                }
            }
            mediaPlayer?.setOnErrorListener { mp, what, extra -> false }
            play()
        }
        //像素保活
        if (mOnepxReceiver == null) {
            mOnepxReceiver = OnepxReceiver()
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.SCREEN_OFF")
        intentFilter.addAction("android.intent.action.SCREEN_ON")
        registerReceiver(mOnepxReceiver, intentFilter)
        //屏幕点亮状态监听，用于单独控制音乐播放
        if (screenStateReceiver == null) {
            screenStateReceiver = ScreenStateReceiver()
        }
        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("_ACTION_SCREEN_OFF")
        intentFilter2.addAction("_ACTION_SCREEN_ON")
        registerReceiver(screenStateReceiver, intentFilter2)
        //启用前台服务，提升优先级
        if (KeepLive.foregroundNotification != null) {
            val intent2 = Intent(applicationContext, NotificationClickReceiver::class.java)
            intent2.action = NotificationClickReceiver.CLICK_NOTIFICATION
            val notification: Notification = NotificationUtils.createNotification(this, KeepLive.foregroundNotification.getTitle(), KeepLive.foregroundNotification.getDescription(), KeepLive.foregroundNotification.getIconRes(), intent2)
            startForeground(13691, notification)
        }
        //绑定守护进程
        try {
            val intent3 = Intent(this, RemoteService::class.java)
            mIsBoundRemoteService = this.bindService(intent3, connection, Context.BIND_ABOVE_CLIENT)
        } catch (e: Exception) {
        }
        //隐藏服务通知
        try {
            if (Build.VERSION.SDK_INT < 25) {
                startService(Intent(this, HideForegroundService::class.java))
            }
        } catch (e: Exception) {
        }
        if (KeepLive.keepLiveService != null) {
            KeepLive.keepLiveService.onWorking()
        }
        return START_STICKY
    }

    private fun play() {
        if (KeepLive.useSilenceMusice) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.start()
            }
        }
    }

    private fun pause() {
        if (KeepLive.useSilenceMusice) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    private inner class ScreenStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == "_ACTION_SCREEN_OFF") {
                isPause = false
                play()
            } else if (intent.action == "_ACTION_SCREEN_ON") {
                isPause = true
                pause()
            }
        }
    }

    private class MyBilder : GuardAidl.Stub() {
        @Throws(RemoteException::class)
        override fun wakeUp(title: String?, discription: String?, iconRes: Int) {
        }
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            if (isServiceRunning(applicationContext, "robin.sdk.keeplive.service.LocalService")) {
                val remoteService = Intent(this@LocalService,
                        RemoteService::class.java)
                startService(remoteService)
                val intent = Intent(this@LocalService, RemoteService::class.java)
                mIsBoundRemoteService = this@LocalService.bindService(intent, this,
                        Context.BIND_ABOVE_CLIENT)
            }
            val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn = pm.isScreenOn
            if (isScreenOn) {
                sendBroadcast(Intent("_ACTION_SCREEN_ON"))
            } else {
                sendBroadcast(Intent("_ACTION_SCREEN_OFF"))
            }
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                if (mBilder != null && KeepLive.foregroundNotification != null) {
                    val guardAidl: GuardAidl = GuardAidl.Stub.asInterface(service)
                    guardAidl.wakeUp(KeepLive.foregroundNotification.getTitle(),
                            KeepLive.foregroundNotification.getDescription(),
                            KeepLive.foregroundNotification.getIconRes())
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (mIsBoundRemoteService) {
                unbindService(connection)
            }
        } catch (e: Exception) {
        }
        try {
            unregisterReceiver(mOnepxReceiver)
            unregisterReceiver(screenStateReceiver)
        } catch (e: Exception) {
        }
        if (KeepLive.keepLiveService != null) {
            KeepLive.keepLiveService.onStop()
        }
    }
}