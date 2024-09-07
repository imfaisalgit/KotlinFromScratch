/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 14: Find the Shortest Distance Between Two Locations on Earth */

// import math functions
import kotlin.math.sin
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

// define Location data class
data class Location(
    val name: String = "",
    var lat: Double,
    val latDir: String,
    var lon: Double,
    val lonDir: String
)

//  global variables and parameters
//  N = north, S = south, E = east, W = west
val L1 = Location(name = "Big Ben", lat=51.5004,
    latDir = "N", lon=0.12143, lonDir = "W")
val L2 = Location(name="Statue of Liberty", lat = 40.689978,
    latDir = "N", lon = 74.045448, lonDir = "W")
val locations = listOf(L1, L2)

val R = 6371.009  // radius of Earth in km

fun main() {
    println("\n*** Measuring Distance between Two Locations on Earth ***\n")
    printLatLong(category = "input", locations)
    val d = haversineDistance()
    printLatLong(category = "adjusted", locations)
    println("\nThe distance between the two given locations:")
    println("d = ${"%10.2f".format(d)} km")
}

fun printLatLong(category: String, locationsToPrint: List<Location>) {
    when(category) {
        "input" ->
            println("...inputted coordinates...\n")
        "adjusted" ->
            println("\n...adjusted coordinates...\n")
    }
    locationsToPrint.forEach { location -> println(location)}
}

fun haversineDistance(): Double {

    // adjust signs based on N/S and E/W directions
    for (location in locations) {
        with(location) {
            if(latDir == "S" && lat > 0.0) lat = - lat
            if(lonDir == "W" && lon > 0.0) lon = - lon
        }
    }
    
    // calculate the angles in radians
    val phi1 = L1.lat * PI/180
    val phi2 = L2.lat * PI/180
    val delPhi = phi2 - phi1
    val delLambda = (L2.lon - L1.lon) * PI/180

    // calculate the distance using haversine formula
    val a = sin(delPhi/2).pow(2) +
            cos(phi1) * cos(phi2) *
            sin(delLambda/2).pow(2)
    // ensure that 0 <= a <= 1 before calculating c
    val c = 2 * asin(sqrt(max(0.0, min(1.0, a))))
    val d = R * c
    return d
}

