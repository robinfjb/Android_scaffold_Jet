package robin.scaffold.track.hook

import android.app.Activity
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import robin.scaffold.track.Constants
import robin.scaffold.track.Utils.getAbsolutePath

class ProxyFrameLayout(private val resumedActivity: Activity) : FrameLayout(resumedActivity) {
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchViewDown = findEventSrcView(event, this)
                if (touchViewDown != null) {
                    Log.d(Constants.TAG, "Activity:" + resumedActivity::class.java.name
                            + "- ACTION_DOWN:" + getAbsolutePath(touchViewDown))
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                val touchViewUp = findEventSrcView(event, this)
                if (touchViewUp != null) {
                    Log.d(Constants.TAG, "Activity:" + resumedActivity::class.java.name
                            + "- ACTION_UP:" + getAbsolutePath(touchViewUp))
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    /**
     * 遍历找到点击的view
     * @param event
     * @param srcView
     * @return
     */
    private fun findEventSrcView(event: MotionEvent, srcView: View): View? {
        if (srcView is ViewGroup) {
            val viewGroup = srcView
            val size = viewGroup.childCount
            for (i in 0 until size) {
                val view = viewGroup.getChildAt(i)
                if (view !is ProxyFrameLayout && isEventInView(event, view)) {
                    val tmpRetView = findEventSrcView(event, view)
                    if (tmpRetView != null) {
                        return tmpRetView
                    }
                }
            }
        } else if (isEventInView(event, srcView)) {
            return srcView
        }
        return null
    }

    /**
     * 判断是否在view的rect范围内
     * @param event
     * @param srcView
     * @return
     */
    private fun isEventInView(event: MotionEvent, srcView: View): Boolean {
        val currentViewRect = Rect()
        if (srcView.getGlobalVisibleRect(currentViewRect)) {
            val rectF = RectF(currentViewRect)
            if (rectF.contains(event.rawX, event.rawY)) {
                return true
            }
        }
        return false
    }
}