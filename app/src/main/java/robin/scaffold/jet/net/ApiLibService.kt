package robin.scaffold.jet.net

import io.reactivex.Single
import retrofit2.http.*


interface ApiLibService {
    @Headers("Content-Type: application/json", "Content-Encoding: gzip")
    @GET("/data/cityinfo/101010100.html")
    fun weather(): Single<LocationResult>
}