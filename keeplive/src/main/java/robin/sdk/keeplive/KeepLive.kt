package robin.sdk.keeplive

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import androidx.annotation.NonNull
import robin.sdk.keeplive.service.JobHandlerService
import robin.sdk.keeplive.service.LocalService
import robin.sdk.keeplive.service.RemoteService


class KeepLive {
    /**
     * 运行模式
     */
    enum class RunMode {
        /**
         * 省电模式
         * 省电一些，但保活效果会差一点
         */
        ENERGY,

        /**
         * 流氓模式
         * 相对耗电，但可造就不死之身
         */
        ROGUE
    }

    companion object {
        lateinit var foregroundNotification: ForegroundNotificationBean
        lateinit var keepLiveService: KeepLiveListener
        lateinit var runMode: RunMode
        var useSilenceMusice = true
    }



    /**
     * 启动保活
     *
     * @param application            your application
     * @param foregroundNotification 前台服务 必须要，安卓8.0后必须有前台通知才能正常启动Service
     * @param keepLiveService        保活业务
     */
    fun startWork(@NonNull application: Application,
                  @NonNull runMode: RunMode,
                  @NonNull foregroundNotification: ForegroundNotificationBean,
                  @NonNull keepLiveService: KeepLiveListener) {
        if (isMain(application)) {
            KeepLive.foregroundNotification = foregroundNotification
            KeepLive.keepLiveService = keepLiveService
            KeepLive.runMode = runMode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //启动定时器，在定时器中启动本地服务和守护进程
                val intent = Intent(application, JobHandlerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    application.startForegroundService(intent)
                } else {
                    application.startService(intent)
                }
            } else {
                //启动本地服务
                val localIntent = Intent(application, LocalService::class.java)
                //启动守护进程
                val guardIntent = Intent(application, RemoteService::class.java)
                application.startService(localIntent)
                application.startService(guardIntent)
            }
        }
    }

    private fun isMain(application: Application): Boolean {
        val pid = Process.myPid()
        var processName = ""
        val mActivityManager = application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfos = mActivityManager.runningAppProcesses
        if (runningAppProcessInfos != null) {
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName
                    break
                }
            }
            val packageName: String = application.getPackageName()
            if (processName == packageName) {
                return true
            }
        }
        return false
    }
}