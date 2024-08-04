
/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 09: Projects 37 */

// Solve the Traveling Salesman Problem with Ant Colony System 


// import block
import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

// input file location: change the datafile location as needed
val datafile = "[insert full filepath]\\berlin52.csv"

// global parameters

val numCities = 52     // set the number of cities
val numAnts = 30       // typically set to 10-30
val rho = 0.1          // rho = 0.1 is recommended for ACS
var pheromone0 = 0.0   // 1/(n*Cnn) for ACS
val q0 = 0.8           // arg max parameter (0.5-0.9)
val zeta = 0.005       // set to a small value
val alpha = 1.0	       // alpha = 1 for ACS
val beta = 2.0         // set to 2-5
val iterMax = 300      // maximum number of iterations
val maxRounds = 50     // number of times the entire process is repeated

// classes and collections

data class City (
    val name: String,
    val node: Int,
    val x: Double,
    val y:Double
)

class Ant(val id: Int, val start: Int) {

    var currentNode = start
    val citiesToVisit = mutableListOf<Int>()
    val pathNodes = ArrayList<Pair<Int, Int>>()
    val pathSegments = mutableListOf<Double>()

    // set fitness to a very high value for function minimization
    var fitness = Double.POSITIVE_INFINITY

    fun setCitiesToVisit() {
        for (i in 0 until  numCities) {
            if (i != this.start)
                this.citiesToVisit += i
        }
    }
}

data class ArgMax (
    val index: Int,
    val value: Double
)

data class Solution (
    val iteration: Int,
    val antID: Int,
    val pathNodes: ArrayList<Pair<Int, Int>>,
    val segments: List<Double>,
    val fitness: Double
)

val cities = mutableListOf<City>()
val ants = mutableListOf<Ant>()
val antSolutions  = mutableListOf<Solution>()
val bestSolutions = mutableListOf<Solution>()

val edges = Array(numCities) {DoubleArray(numCities)}
val pheromone = Array(numCities) {DoubleArray(numCities)}
val prob = Array(numCities) {DoubleArray(numCities)}
var bestOverallTour = ArrayList<Pair<Int, Int>>()

var bestOverallFitness = Double.POSITIVE_INFINITY
var optimaCount = 0

// ---------------------------------------------------------------------

fun main() {

    println("\n*** Solving Berlin52-TSP using Ant Colony System ***\n")
    // conduct pre-processing
    readCities()
    calculateEdges()
    calculatePheromone0()

    // repeat the process “maxRounds” number of times

    for (round in 1..maxRounds) {
        initializePheromone()
        runACS()
        processInterimResults(round)

        // prepare for next iteration
        bestSolutions.clear()
    }

    // print the best overall solution
    printBestOverallFitnessAndTour()
    println("\noptimaCount: $optimaCount")
}

// ---------------------------------------------------------------------

fun readCities() {
    // open input file and read location data
    val file = File(datafile)
    val lines = file.readLines().filterNot{it.isEmpty()}

    for (i in lines.indices) {
        val items = lines[i].split(",")
        if (i == 0) {
            println("Reading data for " + items[0] + "-TSP\n")
        } else {
            // read Name, ID, x, y
            cities += City(
                items[0],
                items[1].trim().toInt(),
                items[2].trim().toDouble(),
                items[3].trim().toDouble()
            )
        }
    }
}

// ---------------------------------------------------------------------

fun calculateEdges() {
    // Assume symmetry: edges[i][j] = edges[j][i]
    for (i in 0 until cities.size) {
        for (j in i until cities.size) {
            if (i == j) {
                edges[i][j] = 0.0
            } else {
                edges[i][j] = sqrt((cities[i].x - cities[j].x).pow(2) +
                        (cities[i].y - cities[j].y).pow(2))
                edges[j][i] = edges[i][j]
            }
        }
    }
}

// ---------------------------------------------------------------------

fun calculatePheromone0() {

    // start at node 0 (first city in the data set)
    var i = 0 // start node for the "nearest neighbor route"
    val citiesToVisitList = (1 until numCities).toMutableList()
    var nearestNeighborTourLength = 0.0

    // build the nearest-neighbor tour
    while (citiesToVisitList.size > 0) {
        // set initial search parameters
        var nearestNode = -9999 // use an unlikely value
        var nearestNodeDistance = Double.MAX_VALUE

        for (j in citiesToVisitList) {
            if (edges[i][j] < nearestNodeDistance) {
                nearestNodeDistance = edges[i][j]
                nearestNode = j
            }
        }

        nearestNeighborTourLength += nearestNodeDistance
        citiesToVisitList.remove(nearestNode)
        i = nearestNode
    }

    // add the edge connecting the last city visited and the starting city
    nearestNeighborTourLength += edges[i][0]

    // calculate initial pheromone value per ACS
    pheromone0 = 1.0/(numCities * nearestNeighborTourLength)
}

// ---------------------------------------------------------------------

fun initializePheromone() {
    // all edges have the same initial pheromone level
    for (i in 0 until numCities) {
        for (j in i until numCities)
            if (i != j) {
                pheromone[i][j] = pheromone0
                pheromone[j][i] = pheromone0
            } else pheromone[i][j] = 0.0
    }
}


// ---------------------------------------------------------------------

fun runACS() {
    var iter = 1
    while(iter <= iterMax) {
        // create a new ant colony
        initializeAnts()

        // generate tours for all ants
        for (ant in ants) {
            ant.setCitiesToVisit()
            buildAntTour(ant)
            antSolutions += Solution(
                iter,
                ant.id,
                ant.pathNodes,
                ant.pathSegments,
                ant.fitness)
        }

        // get the solution with minimum fitness
        val bestAntSolution =
            antSolutions.minWith(compareBy {it.fitness})
        bestSolutions += bestAntSolution

        // apply global pheromone update
        globalPheromoneUpdate()

        // clear previously saved states of ants and solutions
        ants.clear()
        antSolutions.clear()

        // advance the counter
        iter += 1
    }
}

// ---------------------------------------------------------------------

fun initializeAnts() {

    // create a list of nodes (cities) to set start nodes for ants
    val cityList = (0 until numCities).toList()

    // while creating a new Ant, set its start node randomly
    for (i in 0 until numAnts) ants += Ant(i, cityList.random())
}

// ---------------------------------------------------------------------

fun buildAntTour(ant: Ant) {

    var i = ant.currentNode

    while (ant.citiesToVisit.size > 0) {
        // find the next node to visit per ACS
        val nextNode = selectNodeToVisit(i, ant)

        // edge-specific local pheromone update per ACS
        pheromone[i][nextNode] =
            (1- zeta) * pheromone[i][nextNode] +
                    (zeta * pheromone0)
        ant.currentNode = nextNode
        ant.pathNodes += Pair(i, nextNode)
        ant.pathSegments += edges[i][nextNode]
        i = ant.currentNode
        ant.citiesToVisit.remove(nextNode)
    }

    // close the loop by adding the last Pair() of nodes to the path
    // and adding the last path segment to the tour
    ant.pathNodes += Pair(ant.currentNode, ant.start)
    ant.pathSegments += edges[ant.currentNode][ant.start]

    // calculate the fitness of the entire loop (closed path)
    ant.fitness = ant.pathSegments.sum()

    // edge-specific local pheromone update for the last path segment
    pheromone[ant.currentNode][ant.start] =
        ( 1- zeta) * pheromone[ant.currentNode][ant.start] +
                (zeta * pheromone0)
}

// ---------------------------------------------------------------------

fun selectNodeToVisit(i: Int, ant: Ant): Int {

    var chosenNode = -9999 // use an unlikley value
    val argmaxList = mutableListOf<ArgMax>()

    // calculate edge-probabilities and argMaxList elements
    var sum = 0.0
    for (j in ant.citiesToVisit) {
        prob[i][j] = (pheromone[i][j]).pow(alpha) /
                (edges[i][j]).pow(beta)
        sum += prob[i][j]
        argmaxList += ArgMax(j, prob[i][j])
    }

    // calculate normalized values of the edge-probabilities
    for (j in ant.citiesToVisit) {
        prob[i][j] = prob[i][j] / sum
    }

    // use arg max or roulette wheel scheme to select j
    val q = (0 until 1000).random()/1000.0

    if ( q <= q0) {
        // use accumulated experience more greedily per ACS
        val maxArgMax = argmaxList.maxWith(compareBy {it.value})
        chosenNode = maxArgMax.index
    } else {
        // use roulette wheel scheme
        val spin = (0 until 1000).random()/1000.0
        var sumProb = 0.0
        for (j in ant.citiesToVisit) {
            sumProb += prob[i][j]
            if (spin <= sumProb) {
                chosenNode = j
                break
            }
        }
    }
    return chosenNode
}

// ---------------------------------------------------------------------

fun globalPheromoneUpdate() {
    val bestSoFar =
        bestSolutions.minWith(compareBy {it.fitness})
    for (pair in bestSoFar.pathNodes) {
        val (i,j) = pair
        pheromone[i][j] = (1 - rho) * pheromone[i][j] + rho/bestSoFar.fitness
    }
}

// ---------------------------------------------------------------------

fun processInterimResults(round: Int) {
    val bestSoFar =
        bestSolutions.minWith(compareBy {it.fitness})
    if (bestSoFar.fitness < bestOverallFitness) {
        bestOverallFitness = bestSoFar.fitness
        bestOverallTour = bestSoFar.pathNodes
    }

    // print interim results
    println("round: $round iter: ${bestSoFar.iteration}  ant: ${bestSoFar.antID}")
    println("bestSoFar.fitness: ${bestSoFar.fitness}")

    // count the number of times global optima is found
    if (bestSoFar.fitness - 7544.3659 < 0.0001)
        optimaCount += 1
}

// ---------------------------------------------------------------------

fun printBestOverallFitnessAndTour() {

    println("\nbestOverallFitness:  $bestOverallFitness")
    println("\nbestOverallTour: ")

    for (i in bestOverallTour.indices) {
        print("${bestOverallTour[i]}".padEnd(10))
        if ((i+1) % 5 == 0) println()
    }
    println()
}
