package robin.scaffold.track.aspectj

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import robin.scaffold.track.Constants.TAG
import robin.scaffold.track.aspectj.ReflectUtils.getFieldValue


object HookOnClickListener {
    fun onClickListenerHook(view : View) {
        view.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun sendAccessibilityEvent(host: View, eventType: Int) {
                super.sendAccessibilityEvent(host, eventType)
                when (eventType) {
                    AccessibilityEvent.TYPE_VIEW_CLICKED -> Log.d(TAG, "sendAccessibilityEvent: " + host.contentDescription)
                    else -> {
                    }
                }
            }
        }
    }

    fun onScrollListenerHook(recyclerView: RecyclerView) {
        val tmp = getFieldValue(recyclerView, "mScrollListeners")
        var hasHookOnScrollListener = false
        val mScrollListeners = if(tmp != null) {tmp as List<RecyclerView.OnScrollListener>}else null
        if (!mScrollListeners.isNullOrEmpty()) {
            for (listener in mScrollListeners) {
                // 去掉重复的滑动监听
                if (listener is HookOnScrollListener) {
                    hasHookOnScrollListener = true
                }
            }
        }
        if(!hasHookOnScrollListener) {
            recyclerView.addOnScrollListener(HookOnScrollListener())
        }
    }

    fun onRecyclerViewHook(recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter
        recyclerView.adapter = ProxyRecyclerViewAdapter(recyclerView, adapter)
    }

    fun onTextChangedHook(editText : EditText) {
        val tmp = getFieldValue(editText, "mListeners")
        var hasHookTextWatcher = false
        val textWatchers = if(tmp != null) {tmp as ArrayList<TextWatcher>}else null
        if (!textWatchers.isNullOrEmpty()) {
            for (watcher in textWatchers  ) {
                // 去掉重复的滑动监听
                if (watcher is HookTextWatcher) {
                    hasHookTextWatcher = true
                }
            }
        }
        if(!hasHookTextWatcher) {
            editText.addTextChangedListener(HookTextWatcher(editText))
        }
    }

    internal class HookOnScrollListener : RecyclerView.OnScrollListener() {
        private var scrollerState = 0
        private var scrollerX = 0
        private var scrollerY = 0
        override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            scrollerState = newState
            when (scrollerState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                }
                RecyclerView.SCROLL_STATE_IDLE -> {
                    if(recyclerView.layoutManager is LinearLayoutManager) {
                        Log.d(TAG, "afterRecycleViewOnScrollStateChangedMethodCall: " +
                                "first visible item=${(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()}")
                    } else if(recyclerView.layoutManager is GridLayoutManager) {
                        Log.d(TAG, "afterRecycleViewOnScrollStateChangedMethodCall: " +
                                "first visible item=${(recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()}")
                    } else {
                        Log.d(TAG, "afterRecycleViewOnScrollStateChangedMethodCall: " +
                                "x=${scrollerX}, y=${scrollerY}")
                    }
                    // 当页面滚动停止的时候进行数据上报，并且置零参数1
                    scrollerX = 0
                    scrollerY = 0
                }
                else -> {
                }
            }
        }

        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (scrollerState == RecyclerView.SCROLL_STATE_DRAGGING || scrollerState == RecyclerView.SCROLL_STATE_SETTLING) {
                scrollerX += dx
                scrollerY += dy
            }
        }
    }

    internal class HookTextWatcher(private val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.d(TAG, "view:${editText.contentDescription}\nword:$s")
        }
        override fun afterTextChanged(s: Editable) {}
    }
}