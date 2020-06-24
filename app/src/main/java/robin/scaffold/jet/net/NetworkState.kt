package robin.scaffold.jet.net

import android.view.View

data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val INIT = NetworkState(Status.INIT)
        val LOADING = NetworkState(Status.LOADING)
        fun error(msg: String?) = NetworkState(Status.ERROR, msg)

        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
    //页面上loading监听
    val progressVisible: Int
        get() = toVisibility(status == Status.LOADING)
    //页面上重试监听
    val retryVisible:  Int
        get() = toVisibility(status == Status.ERROR)
    //页面上错误显示监听
    val errorVisible:  Int
        get() = toVisibility(msg != null)
}