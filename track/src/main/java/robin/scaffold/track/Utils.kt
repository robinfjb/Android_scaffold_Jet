package robin.scaffold.track

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup

object Utils {
    fun getAbsolutePath(view: View?): String {
        if (view == null) {
            return ""
        }
        if (view.parent == null) {
            return "rootView"
        }
        var path = "";
        var temp = view!!
        while (temp.parent != null && temp.parent is View) {
            var index = 0
            try {
                index = indexOfChild(temp.parent as ViewGroup, temp)
            } catch (e: Exception) {
            }
            path = "${temp.javaClass.simpleName}[$index]/${path}"
            temp = temp.parent as View
        }
        return path
    }
    /**
     * 获取view的viewTree
     * 优化点：
     * 如果这个item有id，则返回id，如果没有，则返回他在ViewTree的第几个
     *
     * @param view
     * @return
     */
    fun getPath(view: View?): String {
        var parentPath = ""
        if (view == null) {
            return parentPath
        }
        parentPath = if (view.parent == null) {
            "rootView"
        } else {
            if (view.parent !is View || TextUtils.isEmpty((view.parent as View).contentDescription)) {
                view.parent.javaClass.simpleName
            } else {
                (view.parent as View).contentDescription.toString()
            }
        }
        var viewType = view.javaClass.simpleName
        var index = 0
        try {
            index = indexOfChild(view.parent as ViewGroup, view)
        } catch (e: Exception) {
        }
        viewType = "$viewType[$index]"
        return "$parentPath/$viewType"
    }

    /**
     * 获取子view在viewTree的第几个
     * 优化点
     * 1：index从"兄弟节点的第几个”优化为:“相同类型兄弟节点的第几个
     *
     * @param parent
     * @param child
     * @return
     */
    private fun indexOfChild(parent: ViewGroup?, child: View): Int {
        if (parent == null) {
            return 0
        }
        val count = parent.childCount
        var j = 0
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            if (child.javaClass.isInstance(view)) {
                if (view === child) {
                    return j
                }
                j++
            }
        }
        return -1
    }

}