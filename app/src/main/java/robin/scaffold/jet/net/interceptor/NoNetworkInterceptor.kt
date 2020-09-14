package robin.scaffold.jet.net.interceptor

import android.content.Context
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import robin.scaffold.jet.utils.isNetworkConnected

/**
 * 当无网络的情况下打开我们的 App 时，也能获取到上一次的数据
 */
class NoNetworkInterceptor(private val context : Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        if(!isNetworkConnected(context)) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }
}