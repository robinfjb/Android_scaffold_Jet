package robin.scaffold.track.hook

import android.app.Activity
import android.util.Log
import android.view.Choreographer
import robin.scaffold.track.Constants
import java.lang.ref.WeakReference

internal class LogFrameCallback : Choreographer.FrameCallback {
    private val MONITOR_INTERVAL = 200L;//设置获取fps的时间为200ms
    private val MONITOR_INTERVAL_NANOS = MONITOR_INTERVAL * 1000L * 1000L
    private val MAX_INTERVAL = 1000L //设置计算fps的单位时间间隔1000ms,即fps/s
    private var mFpsCount = 0
    private var mFrameTimeNanos: Long = 0 //本次的当前时间
    private var weakReference: WeakReference<Activity>? = null
    var isCanwork = false
    fun reset() {
        isCanwork = false
        mFpsCount = 0
        mFrameTimeNanos = 0
    }
    fun setActivity(activity: Activity) {
        weakReference = WeakReference(activity)
    }
    override fun doFrame(frameTimeNanos: Long) {
        if(mFrameTimeNanos == 0L) {
            mFrameTimeNanos = frameTimeNanos
        }
        mFpsCount++;

        val interval: Long = (frameTimeNanos - mFrameTimeNanos)
        if (interval > MONITOR_INTERVAL_NANOS) {
            val fps = (mFpsCount * 1000L * 1000L).toDouble() / interval * MAX_INTERVAL
            Log.d(Constants.TAG, "activity: ${weakReference?.get()?.javaClass!!.name}--fps =$fps")
            mFpsCount = 0
            mFrameTimeNanos = frameTimeNanos;
        }
        if (isCanwork) {
            //注册下一帧回调
            Choreographer.getInstance().postFrameCallback(this);
        }
    }
}