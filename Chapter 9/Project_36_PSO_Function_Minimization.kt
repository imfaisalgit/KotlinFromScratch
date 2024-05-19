/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 09: Projects 36 */

// Particle Swarm Optimization

import kotlin.math.sin
import kotlin.math.pow
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.min
import kotlin.math.max

data class Solution(
    var pos: DoubleArray,
    var fitness: Double
)

data class Particle(
    val id: Int,
    val pos: DoubleArray,
    val vel: DoubleArray,
    var fitness: Double,
    val pBest: Solution
)

// problem definition
val getFitness = ::eggHolder

val nDim = 2    // number of variables in the cost function
val xBbounds = arrayOf(doubleArrayOf(-512.0, 512.0),
    doubleArrayOf(-512.0, 512.0))
val xRange = doubleArrayOf(xBbounds[0][1] - xBbounds[0][0],
    xBbounds[1][1] - xBbounds[1][0])
val lambda = 0.5
val vMax = doubleArrayOf(lambda*xRange[0], lambda*xRange[1])

// global parameters
val TMAX = 50
val SWARMSIZE = 30
val wmax = 1.2
val wmin = 0.5
val wt = (wmax - wmin)/TMAX
var w = wmax
val c1 = 2.0       // cognitive coefficient
val c2 = 2.0       // social coefficient

// global objects and collections
val swarm = mutableListOf<Particle>()
val BestSolution = Solution(doubleArrayOf(0.0, 0.0), Double.MAX_VALUE)

// ---------------------------------------------------------------------

fun main() {
    println("\n*** Real-valued function optimization using PSO ***\n")
    println("Function dimensions: $nDim")
    println("Swarm size: $SWARMSIZE, Max time steps: $TMAX")
    println("w_max: $wmax  w_min: $wmin")
    println("Cognitive factor (c1): $c1")
    println("Social factor (c2): $c2")

    // initialize the swarm
    initSwarm()

    // run PSO algorithm
    runPSO()

    // print final results
    println("\nBest solution after $TMAX iterations:")
    println(BestSolution)
}

// ---------------------------------------------------------------------

fun initSwarm() {
    println("\nStarting initialization...")

    for (i in 0 until SWARMSIZE) {
        // define local objects
        val pos = DoubleArray(nDim)
        val vel = DoubleArray(nDim)
        val fitness: Double
        val pBest: Solution

        // set initial positions (random, within bounds)
        for (j in 0 until nDim) {
            pos[j] = xBbounds[j][0] + (xBbounds[j][1] - xBbounds[j][0]) *
                    (0..1000).random() / 1000.0
            vel[j] = 0.0
        }

        // add new particles to the swarm
        fitness = getFitness(pos)
        pBest = Solution(pos.copyOf(), fitness)
        swarm += Particle(i, pos, vel, fitness, pBest)

        // update BestSolution
        if (fitness < BestSolution.fitness) {
            BestSolution.pos = pos.copyOf()
            BestSolution.fitness = fitness
        }
    }

    println("\nBest solution after initialization:")
    println(BestSolution)
}

// ---------------------------------------------------------------------

fun runPSO() {
    for (timeStep in 0 until TMAX) {
        // update inertia factor as a function of time
        val w = wmax - timeStep * wt

        // random coefficients for cognitive and social components
        val r1 = (0..100).random()/100.0
        val r2 = (0..100).random()/100.0

        // iterate over each particle of the swarm
        for (i in swarm.indices) {
            // update velocity and position vectors
            for (j in 0 until nDim) {
                // update velocity vector and implement bounds
                val C1 = w * swarm[i].vel[j]
                val C2 = c1 * r1 * (swarm[i].pBest.pos[j]-swarm[i].pos[j])
                val C3 = c2 * r2 * (BestSolution.pos[j] - swarm[i].pos[j])
                val vel = C1 + C2 + C3

                // implement velocity bounds
                swarm[i].vel[j] = min(max(vel, -vMax[j]), vMax[j])

                // update the position vector and implement bounds
                swarm[i].pos[j] += swarm[i].vel[j]
                swarm[i].pos[j] =
                    min(max(swarm[i].pos[j], xBbounds[j][0]), xBbounds[j][1])
            }

            // evaluate particle fitness
            swarm[i].fitness = getFitness(swarm[i].pos)

            // update particle's best solution (pBest)
            if (swarm[i].fitness < swarm[i].pBest.fitness) {
                swarm[i].pBest.pos = swarm[i].pos.copyOf()
                swarm[i].pBest.fitness = swarm[i].fitness
            }

            // update the global best solution
            if (swarm[i].fitness < BestSolution.fitness) {
                BestSolution.pos = swarm[i].pos.copyOf()
                BestSolution.fitness = swarm[i].fitness
            }
        }
    }
}

// ---------------------------------------------------------------------

fun eggHolder(x: DoubleArray): Double {
    val c1 = (x[1] + 47)
    val c2 = sin( sqrt( abs( 0.5 * x[0] + c1 ) ) )
    val c3 = x[0] * sin( sqrt( abs( x[0] - c1 ) ) )

    // multiply by -1 ONLY for minimization problems
    return (-c1 * c2 - c3)
}
