package robin.scaffold.track.hook

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Choreographer
import android.view.ViewGroup
import android.view.ViewTreeObserver
import robin.scaffold.track.Constants.TAG
import robin.scaffold.track.aspectj.TrackerAspect.isColdStart
import java.util.*

internal class HookActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    private var resumeActivityCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityTrack[1] = activity.javaClass.name
    }
    override fun onActivityStarted(activity: Activity) {
        resumeActivityCount ++
    }
    override fun onActivityResumed(activity: Activity) {
        if (!activityNameSet.contains(activity.javaClass.name)) {
            val viewGroup = activity.window.decorView as ViewGroup
            if (viewGroup != null) {
                val size = viewGroup.childCount
                val customFrameLayout = ProxyFrameLayout(activity)
                for (i in 0 until size) {
                    val view = viewGroup.getChildAt(i)
                    if (view != null) {
                        viewGroup.removeView(view)
                        customFrameLayout.addView(view)
                    }
                }
                viewGroup.addView(customFrameLayout)
            }
            activityNameSet.add(activity.javaClass.name)
        }
        logLaunchTime(activity)
        logFps(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        activityTrack[0] = activity.javaClass.name
        mActivityLauncherTimeStamp = SystemClock.elapsedRealtime();

        pauseFps(activity)
    }
    override fun onActivityStopped(activity: Activity) {
        resumeActivityCount --
        //  退后台，GC 找LeakActivity
        if (resumeActivityCount == 0) {
            logMemoLeak(activity)
        }
    }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        mActivityStringWeakHashMap[activity] = activity.javaClass.name;
    }

    private fun resetActivityTrack() {
        activityTrack[0] = null
        activityTrack[1] = null
    }

    //launch time
    private fun logLaunchTime(activity: Activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            //对于Android 10以后的系统，可以在onActivityResumed回调时添加一UI线程Message来达到监听目的
            Handler(activity.mainLooper).post {
                logLaunchTimeInternal(activity)
            }
        } else {
            activity.window.decorView.viewTreeObserver.addOnWindowFocusChangeListener(object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(b: Boolean) {
                    if (b) {
                        logLaunchTimeInternal(activity)
                        activity.window.decorView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
        }
    }
    private fun logLaunchTimeInternal(activity: Activity) {
        if(isColdStart) {
            isColdStart = false
            val coldLauncherTime = SystemClock.elapsedRealtime() - sStartUpTimeStamp
            Log.d(TAG, "coldLauncherTime=$coldLauncherTime")
        }
        if(!isColdStart && mActivityLauncherTimeStamp > 0
                && activityTrack[0] != null && activityTrack[0] != activity.javaClass.name
                && activityTrack[1] != null && activityTrack[1] == activity.javaClass.name) {
            resetActivityTrack()
            val activityLauncherTime = SystemClock.elapsedRealtime() - mActivityLauncherTimeStamp
            Log.d(TAG, "activity:${activity.javaClass.name}:activityLauncherTime=$activityLauncherTime")
        }
    }
    //FPS
    private val frameCallback = LogFrameCallback()
    private fun logFps(activity: Activity) {
        frameCallback.reset()
        frameCallback.isCanwork = true
        frameCallback.setActivity(activity)
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun pauseFps(activity: Activity) {
        frameCallback.reset()
        frameCallback.isCanwork = false
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }
    //Memo leak
    //在APP进入后台之后主动触发一次GC，然后延时10s，
    // 进行检查，之所以延时10s，是因为GC不是同步的，
    // 为了让GC操作能够顺利执行完，这里选择10s后检查。
    // 在检查前分配一个4M的大内存块，再次确保GC执行，
    // 之后就可以根据WeakHashMap的特性，
    // 查找有多少Activity还保留在其中，这些Activity就是泄露Activity。
    private val mActivityStringWeakHashMap: WeakHashMap<Activity, String> = WeakHashMap()
    private fun logMemoLeak(activity: Activity) {
        val handler = Handler(activity.mainLooper)
        handler.postDelayed({
            mallocBigMem()
            Runtime.getRuntime().gc()
        }, 500)
        handler.postDelayed({
            if (resumeActivityCount == 0) {
                //  分配大点内存促进GC
                mallocBigMem()
                Runtime.getRuntime().gc()
                SystemClock.sleep(100)
                System.runFinalization()
                val hashMap: HashMap<String, Int> = HashMap()
                for ((key) in mActivityStringWeakHashMap) {
                    val name = key.javaClass.name
                    val value = hashMap[name]
                    if (value == null) {
                        hashMap[name] = 1
                    } else {
                        hashMap[name] = value + 1
                    }
                }
                hashMap.forEach {
                    Log.e(TAG, "activity:${it.key}:leak time =${it.value}")
                }
            }
        }, 10*1000)
    }

    private fun mallocBigMem() {
        val leakHelpBytes = ByteArray(4 * 1024 * 1024)
        var i = 0
        while (i < leakHelpBytes.size) {
            leakHelpBytes[i] = 1
            i += 1024
        }
    }

    companion object {
        var activityNameSet: MutableSet<String> = HashSet()
        var sStartUpTimeStamp: Long = 0
        var mActivityLauncherTimeStamp : Long = 0
        var activityTrack: Array<String?> = arrayOfNulls(2)//out in
    }
}