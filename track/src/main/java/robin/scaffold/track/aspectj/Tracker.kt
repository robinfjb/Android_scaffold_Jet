package robin.scaffold.track.aspectj

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.OnHierarchyChangeListener
import android.widget.EditText
import androidx.appcompat.widget.ContentFrameLayout
import androidx.recyclerview.widget.RecyclerView
import robin.scaffold.track.Utils.getPath
import robin.scaffold.track.aspectj.HookOnClickListener.onClickListenerHook
import robin.scaffold.track.aspectj.HookOnClickListener.onRecyclerViewHook
import robin.scaffold.track.aspectj.HookOnClickListener.onScrollListenerHook
import robin.scaffold.track.aspectj.HookOnClickListener.onTextChangedHook


object Tracker {
    /**
     * 开始进行页面的埋点，获取页面的根布局
     *
     * @param activity
     */
    fun startViewTracker(activity: Activity) {
        startViewTracker(activity.window.decorView)
    }

    fun startViewTracker(view: View) {
        startViewTracker(view, getPath(view))
    }

    fun startViewTracker(view: View, parentContentDescription: String?) {
        setViewTracker(view, parentContentDescription)
    }

    /**
     * 给View设置setContentDescription以及监听
     *
     * @param view
     */
    fun setViewTracker(view: View) {
        setViewTracker(view, getPath(view))
    }

    fun setViewTracker(view: View, contentDescription: String?) {
        view.contentDescription = contentDescription
        onClickListenerHook(view)
        if (view is RecyclerView) {
            onScrollListenerHook(view as RecyclerView)
            onRecyclerViewHook(view as RecyclerView)
        } else if (view is ViewGroup) {
            setChildViewTracker(view as ViewGroup)
        } else if (view is EditText) {
            onTextChangedHook(view as EditText)
        }
    }

    /**
     * 给ViewGroup的View添加埋点
     *
     * @param viewGroup
     */
    private fun setChildViewTracker(viewGroup: ViewGroup) {
        // 给ViewGroup添加监听，如果有页面元素的变化，重新构建其tracker
        viewGroup.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View?) {
                setViewTracker(parent)
            }

            override fun onChildViewRemoved(parent: View, child: View?) {
                setViewTracker(parent)
            }
        })
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            setViewTracker(viewGroup.getChildAt(i))
        }
    }



    /**
     * @param view
     * @param recyclerView
     * @param position
     * @return
     */
    fun getRecyclerViewPath(view: View?, recyclerView: RecyclerView, position: Int): String {
        var parentPath = ""
        if (view == null) {
            return parentPath
        }
        parentPath = if (view.parent is ContentFrameLayout) {
            "rootView"
        } else {
            recyclerView.contentDescription?.toString() ?: ""
        }
        var viewType = view.javaClass.simpleName
        viewType = "$viewType[position$position]"
        return "$parentPath/$viewType"
    }
}