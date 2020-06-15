package robin.scaffold.jet.net

data class Wifi(
    val macAddress: String,
    val ssid: String,
    val signalStrength: Int,
    val channel: Int,
    val frequency: Int ?= null,
    val connected: Boolean?= null,
    val timestamp: Long ?= null
)

data class Location(
    val id: String?= null,
    val timestamp: Long,
    val wifis: List<Wifi>
)

data class Track(
        val location: Location,
        val timestamp: Long
)


data class LocationResult(
    val asset: String = "",
    val location: WayzLocation
)

data class WayzLocation(
        val position: Position?,
        val address: Address?
)

data class Position(
    val point: Point?
    // val source: String
)

data class Address(val name: String)

data class Point(
    val altitude: Double?,
    val latitude: Double,
    val longitude: Double
)