package robin.scaffold.jet.net

import androidx.annotation.Keep
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Keep
class SdkNetApi  {

    private var appClient: OkHttpClient?= null

    private var retrofit: Retrofit?= null

    private val apiLibService: ApiLibService by lazy(LazyThreadSafetyMode.NONE) {
        if(appClient == null) {
            appClient = getClient()
        }

        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(appClient)
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        retrofit!!.create(ApiLibService::class.java)
    }

    private fun getClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.addInterceptor(GzipRequestInterceptor())

        return httpClientBuilder.build()
    }

    fun getWeather(): Single<LocationResult> {
        return apiLibService.weather();
    }

    companion object {
        const val API_BASE_URL = "http://www.weather.com.cn"
    }
}