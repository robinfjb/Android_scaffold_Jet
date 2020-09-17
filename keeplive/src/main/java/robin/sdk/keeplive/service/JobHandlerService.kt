package robin.sdk.keeplive.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import robin.sdk.keeplive.KeepLive
import robin.sdk.keeplive.NotificationUtils
import robin.sdk.keeplive.Util.isRunningTaskExist
import robin.sdk.keeplive.Util.isServiceRunning
import robin.sdk.keeplive.receiver.NotificationClickReceiver


class JobHandlerService : JobService() {
    private lateinit var mJobScheduler: JobScheduler
    private val jobId = 100

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.startService(this)
        if (VERSION.SDK_INT >= 21) {
            mJobScheduler = getSystemService("jobscheduler") as JobScheduler
            mJobScheduler.cancel(jobId)
            val builder = JobInfo.Builder(jobId, ComponentName(this.packageName, JobHandlerService::class.java.name))
            if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMinimumLatency(30000L)
                builder.setOverrideDeadline(30000L)
                builder.setMinimumLatency(30000L)
                builder.setBackoffCriteria(30000L, 0)
            } else {
                builder.setPeriodic(30000L)
            }
            builder.setRequiredNetworkType(1)
            builder.setPersisted(true)
            mJobScheduler.schedule(builder.build())
        }
        return Service.START_STICKY
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        if (!isServiceRunning(applicationContext, "robin.sdk.keeplive.service.LocalService")
                || !isRunningTaskExist(applicationContext, "$packageName:remote")) {
            startService(this);
        }

        return false;
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (!isServiceRunning(applicationContext, "robin.sdk.keeplive.service.LocalService")
                || !isRunningTaskExist(applicationContext, "$packageName:remote")) {
            startService(this);
        }

        return false;
    }

    private fun startService(context: Context) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O && KeepLive.foregroundNotification != null) {
            val intent = Intent(this.applicationContext, NotificationClickReceiver::class.java)
            intent.action = "CLICK_NOTIFICATION"
            val notification: Notification = NotificationUtils.createNotification(this,
                    KeepLive.foregroundNotification.getTitle(),
                    KeepLive.foregroundNotification.getDescription(),
                    KeepLive.foregroundNotification.getIconRes(),
                    intent)
            startForeground(13691, notification)
        }
        val localIntent = Intent(context, LocalService::class.java)
        val guardIntent = Intent(context, RemoteService::class.java)
        startService(localIntent)
        startService(guardIntent)
    }
}