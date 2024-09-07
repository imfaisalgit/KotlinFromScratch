/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 35: Optimize a Multivariate Function with the Genetic Algorithm */

// import block
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max
import kotlin.random.Random

// define required data classes
data class Solution(
    val chromosome: DoubleArray,
    val fitness: Double
)

// global parameters and declarations
val getFitness = :: eggHolder

val chromosomeLength = 2    // number of independent variables
val bounds = arrayOf(doubleArrayOf(-512.0, 512.0),
                     doubleArrayOf(-512.0, 512.0))
val varRange = doubleArrayOf(bounds[0][1] - bounds[0][0],
                             bounds[1][1] - bounds[1][0])

val POP_SIZE = 100
val MAX_GEN = 200
val MUTATION_THRESHOLD = 0.5	// on average, 1 of 2 genes will mutate
val MUTATION_FACTOR = 0.02
val ELITISM = 0.1
val eliteSize = (POP_SIZE * ELITISM).toInt()

val population: MutableList<Solution> = mutableListOf()
val nextgen: MutableList<Solution> = mutableListOf()
val bestSolutions: MutableList<Solution> = mutableListOf()

// -----------------------------------------------------------------------------

fun main() {

    println("\n*** Real-valued function optimization using genetic algorithm ***\n")
    println("Number of dimensions: $chromosomeLength")
    println("Population size: $POP_SIZE, Generations: $MAX_GEN")
    println("Elitism: $ELITISM")
    println("Mutation threshold: $MUTATION_THRESHOLD")
    println("Mutation factor: $MUTATION_FACTOR")

    // initialize the population
    initPopulation()

    // run genetic algorithm
    runGA()

    // print best-overall solution
    printBestSolution()
}

// -----------------------------------------------------------------------------

fun printBestSolution() {
    // sort the bestSolutions to get the best-so-far solution
    bestSolutions.sortByDescending {it.fitness}
    println("\nBest solution found after $MAX_GEN generations:")

    // deconstruct for printing with formatting
    val (chromosome, fitness) = bestSolutions[0]

    // format and print the best-so-far properties
    for (i in chromosome.indices) {
        print("chromosome[$i]: ")
        println("%5.8f".format(chromosome[i]))
    }
    println("Fitness: " + "%5.5f".format(-fitness))
}

// -----------------------------------------------------------------------------

fun initPopulation() {
    // initialize a population of valid solutions (genes within bounds)
    // each solution is represented by a chromosome

    for (person in 0 until POP_SIZE) {
        val x = DoubleArray(chromosomeLength)
        for (i in 0 until chromosomeLength) {
            // first argument is inclusive, second one is not
            // possible to add a small bias term to the upper bounds
            x[i] = Random.nextDouble(bounds[i][0], bounds[i][1])
        }
        population += Solution(x, getFitness(x))
    }

    // sort population (in-place) in descending order
    population.sortByDescending {it.fitness}

    println("\nBest solution from initial population:")
    print(population[0].chromosome.contentToString())
    println(" ${-population[0].fitness}")
    println("\n... initPopulation done ...\n")
}

// -----------------------------------------------------------------------------

fun eggHolder(x: DoubleArray): Double {
    val c1 = (x[1] + 47)
    val c2 = sin( sqrt( abs( 0.5 * x[0] + c1 ) ) )
    val c3 = x[0] * sin( sqrt( abs( x[0] - c1 ) ) )

    // multiply by -1 ONLY for minimization problems
    return -1.0 * (-c1 * c2 - c3)
}

// -----------------------------------------------------------------------------

fun runGA() {
    // run the algorithm for specified number of generations
    for (generation in 1 .. MAX_GEN) {

        // step 1: implement elitism
        selectElites()

        // step 2: implement crossover and mutation
        for (i in eliteSize until POP_SIZE) {
            // select parents for crossover
            val parent1 = tournament()
            val parent2 = tournament()

            // produce a child using crossover and mutation
            val child = crossover(parent1, parent2)
            // add child to nextgen
            nextgen += child
        }

        // step 3: transfer nextgen to current population
        for (i in nextgen.indices)
            population[i] = nextgen[i].copy()

        // step 4: clear nextgen for next iteration
        nextgen.clear()

        // step 5: sort population in descending order (in-place)
        population.sortByDescending {it.fitness}

        // step 6: add the fittest solution to bestSolutions
        bestSolutions += population[0]

        // step 7: print the fittest solution
        printSolution(generation, population[0])
    }
}

// -----------------------------------------------------------------------------

fun selectElites() {
    // assign top “eliteSize” individuals to nextgen
    for (i in 0 until eliteSize)
        nextgen += population[i].copy()
}

// -----------------------------------------------------------------------------

fun tournament(): Solution {
    // random sampling with replacement
    // use the entire population including elites
    val candidate1 = population.random().copy()
    val candidate2 = population.random().copy()
    // return the winner of the tournament
    return if (candidate1.fitness >= candidate2.fitness) candidate1
    else candidate2
}

// -----------------------------------------------------------------------------

fun crossover(parent1: Solution, parent2: Solution): Solution {
    // select a random weight within (0-1)
    // this could be generated separately for x and y components
    val s = (0..1000).random()/1000.0

    // generate randomly weighted genes
    var x1 = parent1.chromosome[0]*s + parent2.chromosome[0]*(1-s)
    var x2 = parent1.chromosome[1]*s + parent2.chromosome[1]*(1-s)

    // check that new genes stay within bounds (decision space)
    x1 = min(max(x1, bounds[0][0]), bounds[0][1])
    x2 = min(max(x2, bounds[1][0]), bounds[1][1])

    // compose the new chromosome
    val xNew = doubleArrayOf(x1, x2)
    // mutate the new chromosome
    mutation(xNew)

    return Solution(xNew, getFitness(xNew))
}

// -----------------------------------------------------------------------------

fun mutation(xNew: DoubleArray) {
    for (i in 0 until chromosomeLength) {
        if (((0..1000).random() / 1000.0) <= MUTATION_THRESHOLD) {
            // get the random sign factor
            val sign = if ((0..100).random()/100.0 <= 0.5) -1 else 1
            xNew[i] += sign * varRange[i] * MUTATION_FACTOR
            xNew[i] =  min(max(xNew[i], bounds[i][0]), bounds[i][1])
        }
    }
    // nothing to return
}

// -----------------------------------------------------------------------------

fun printSolution(generation: Int, solution: Solution) {
    val str1 = "%04d".format(generation).padEnd(10, ' ')
    val (c, f) = solution
    val str2 = "%5.7f".format(c[0]).padEnd(14, ' ')
    val str3 = "%5.7f".format(c[1]).padEnd(14, ' ')

    // multiply "f" by -1 for minimization (for display only)
    val str4 = "%5.4f".format(-f)

    println(str1 + str2 + str3 + str4)
}


