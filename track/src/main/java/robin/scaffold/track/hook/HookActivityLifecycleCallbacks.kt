package robin.scaffold.track.hook

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.ViewGroup
import java.util.*

internal class HookActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
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

        activity.window.decorView.viewTreeObserver.addOnWindowFocusChangeListener {
            if(it) {
//                val isColdStarUp = ActivityStack.
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        var activityNameSet: MutableSet<String> = HashSet()
    }
}