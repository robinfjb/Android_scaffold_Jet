package robin.scaffold.jet.net.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RetryInterceptor : Interceptor {
    companion object {
        private const val MAX_RETRY_COUNT = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        while (!response.isSuccessful && tryCount < MAX_RETRY_COUNT) {
            tryCount++
            response = chain.proceed(request)
        }
        return response
    }
}
