/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Chapter 04: Projects 09-12 */


// -------------------- Project 09 --------------------

// Find the Square Root with the Babylonian Algorithm

fun main() {
    println("\n*** Finding Square Root Using Babylonian Algorithm ***\n")
    println("Enter a number (>=1) to find its square root:")
    val num = readln().toDouble()
    println("You have entered: $num\n")
    val squareRoot = babylonianSquareRoot(num)
    println("\nThe estimated square root of $num is: $squareRoot\n")
}

fun babylonianSquareRoot(num: Double): Double {
    val tolerance = 0.000001
    var iter = 1
    var guess = num / 2.0
    while(Math.abs(guess * guess - num) > tolerance) {
        println("iter: $iter  guess=$guess")
        guess = (guess + num / guess) / 2.0
        iter ++
    }
    return guess
}

// -------------------- Project 10 --------------------

// Create Pythagorean Triples with Euclid’s Formula

fun main() {
    var m = 2               // value of m
    var n = 1               // value of n
    val numTriples = 10     // number of triples

    println("\n*** Pythagorean Triples Using Euclid's Formula ***\n")
    println("Number of Pythagorean triples: $numTriples\n")

    // generate the first “numTriples” triples
    for (i in 1..numTriples) {
        val pythagoreanTriple = generatePythagoreanTriple(m, n)
        print("i=${"%2d".format(i)}    " +
                "m=${"%2d".format(m)}    n=${"%2d".format(n)}  ")
        println("Pythagorean triple: $pythagoreanTriple")
        n++
        m++
    }
}

fun generatePythagoreanTriple(m: Int, n: Int):
        Triple<Int, Int, Int> {
    val a = m * m - n * n
    val b = 2 * m * n
    val c = m * m + n * n
    return Triple(a, b, c)
}

// -------------------- Project 11 --------------------

// Identify Prime Numbers with the Sieve of Eratosthenes

fun main() {
    println("\n*** Find ALL Prime Numbers Up To ‘n’ ***\n")
    println("Enter a number > 2 to generate the list of primes:")
    val num: Int = readln().toInt()
    println("You have entered: $num")

    val primeNumbers = sieveOfEratosthenes(num)
    println("\nThe prime numbers <= $num are:")
    printPrimes(primeNumbers)
}

fun printPrimes(primeNumbers: List<Int>) {
    for (i in primeNumbers.indices) {
        if (i != 0 && i % 6 == 0) println()
        print("${"%8d".format(primeNumbers[i])} ")
    }
}

fun sieveOfEratosthenes(n: Int): List<Int> {
    // create a boolean array with all values set to 'true'
    val primes = BooleanArray(n + 1) { true }
    // create a mutable list of integers to save prime numbers
    val primeNumbers = mutableListOf<Int>()

    // set 0 and 1 to not be prime
    primes[0] = false
    primes[1] = false

    // iterate over all numbers until i^2 > N
    var i = 2
    while (i*i <= n) {
        // if 'i' is prime, mark all multiples of i as not prime
        if (primes[i]) {
            for (j in i * i..n step i) {
                primes[j] = false
            }
        }
        i++
    }
    // collect all prime numbers into a list and return it
    for ((index, value) in primes.withIndex())
        if (value) primeNumbers.add(index)

    return primeNumbers
}

// -------------------- Project 12 --------------------

// Calculate the Earth’s Circumference the Ancient Way

import kotlin.math.atan

data class Earth(
    val alpha: Double,
    val circumference: Int,
    val radius: Int
)

fun calculateEarthMetrics(s1: Double, h1: Double,
                          s2: Double, h2:Double, d: Double): Earth {
    // calculate the angles of the shadows
    val theta1 = atan((s1 / h1))
    val theta2 = atan(s2 / h2)

    // calculate the angle at the center of the Earth
    val alpha = theta2 - theta1

    // calculate the circumference and radius
    val circumference = (2 * Math.PI * d / alpha).toInt()
    val radius = (d / alpha).toInt()

    return Earth(alpha, circumference, radius)
}

fun main() {
    
    // known values
    val shadow1 = 0.0    // m
    val height1 = 7.0    // m
    val shadow2 = 0.884  // m
    val height2 = 7.0    // m
    val distanceBetweenCities = 800.0 // in km
    val (alpha, circumference, radius) =
        calculateEarthMetrics(s1=shadow1, h1=height1,
            s2=shadow2, h2=height2,
            d=distanceBetweenCities)

    // output the estimated circumference and radius
    println("\n*** Measuring the Earth's Circumference and Radius ***\n")
    println("Angle (alpha): ${"%7.5f".format(alpha)} radian")
    println("Circumference: $circumference kilometers")
    println("Radius: $radius kilometers")
}

