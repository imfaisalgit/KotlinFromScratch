/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 05: Projects 17 */

// The flight of a cannonball

// import math functions
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.math.pow
import kotlin.math.abs

// set global parameters
val v0 = 70             // m/s
val g = 9.8             // m/s2
val h0 = 25             // m
val target = 400        // m
val TOL = 1.0e-7

private val f = :: projectile

// the interval [x1, x2] needs to enclose the root
val x1 = 10.0    //in degrees
val x2 = 30.0    //in degrees

// ------------------------------------------------------------------------

fun main() {
    println("\n*** Firing angle for hitting a target ***\n")

    if (f(x1) * f(x2) < 0) {
        println("...Initial guesses are valid...")
        val root = bisection(x1, x2)
        val rootFormatted = String.format("%.2f", root)
        println("The firing angle to hit the target is:" +
                "\n$rootFormatted degrees")
    } else {
        println("\n...Initial guesses are not valid...\n")
    }
}

// ------------------------------------------------------------------------

fun projectile(angle: Double): Double {
    val x = angle * PI / 180.0
    return target - (v0 * cos(x) / g) *
            (v0 * sin(x) + sqrt((v0 * sin(x)).pow(2) + 2 * g * h0))
}

// ------------------------------------------------------------------------

fun bisection(_x1: Double, _x2: Double): Double {
    var x1 = _x1
    var x2 = _x2
    var x = (x1 + x2) / 2.0

    while (abs(f(x)) >= TOL) {
        if (f(x) * f(x2) > 0 ) {
            x2 = x
        } else x1 = x
        x = (x1 + x2) / 2.0
    }
    return x
}
