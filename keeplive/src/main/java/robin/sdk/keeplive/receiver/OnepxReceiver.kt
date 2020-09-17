package robin.sdk.keeplive.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import robin.sdk.keeplive.activity.OnePixActivity


class OnepxReceiver : BroadcastReceiver(){
    private val mHander: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    var screenOn = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {    //屏幕关闭的时候接受到广播
            screenOn = false
            mHander!!.postDelayed({
                if (!screenOn) {
                    val intent2 = Intent(context, OnePixActivity::class.java)
                    intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)
                    try {
                        pendingIntent.send()
                        /*} catch (PendingIntent.CanceledException e) {*/
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }, 1000)
            //通知屏幕已关闭，开始播放无声音乐
            context.sendBroadcast(Intent("_ACTION_SCREEN_OFF"))
        } else if (intent.action == Intent.ACTION_SCREEN_ON) {   //屏幕打开的时候发送广播  结束一像素
            screenOn = true
            //通知屏幕已点亮，停止播放无声音乐
            context.sendBroadcast(Intent("_ACTION_SCREEN_ON"))
        }
    }
}