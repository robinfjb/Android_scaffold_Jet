package robin.scaffold.jet.repo

import io.reactivex.Single
import robin.scaffold.jet.net.SdkNetApi
import robin.scaffold.jet.net.WeatherResult
import javax.inject.Inject

class HomeRepository @Inject constructor(){
    private val sdkNetApi : SdkNetApi by lazy(LazyThreadSafetyMode.NONE) {
        SdkNetApi()
    }

    fun getWeather() : Single<WeatherResult> {
        return sdkNetApi.getWeather()
    }
}