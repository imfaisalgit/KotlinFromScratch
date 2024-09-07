/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 34: Solve the Knapsack Problem */

import kotlin.math.roundToInt

// define required data classes
data class Solution(val chromosome: IntArray  , val fitness: Int)
data class Item(val value: Int, val weight: Int)

// define the basket of items
private val items: List<Item> = listOf(
    Item(75, 15),
    Item(55, 32),
    Item(50, 30),
    Item(68, 43),
    Item(62, 54),
    Item(45, 38),
    Item(68, 62),
    Item(84, 85),
    Item(87, 87),
    Item(95, 83),
    Item(35, 21),
    Item(63, 53)
)

val chromosomeLength = items.size
val maxWeight = 175

// global parameters and declarations
val POP_SIZE = 25
val MAX_GEN = 30
val ELITISM = 0.1
val eliteSize = (POP_SIZE * ELITISM).toInt()

/* limit the mutation threshold value to three decimal places */
val MUTATION_THRESHOLD =
    ((1.0/chromosomeLength)*1000.0).roundToInt() / 1000.0

val population: MutableList<Solution> = mutableListOf()
val nextgen: MutableList<Solution> = mutableListOf()
val bestSolutions: MutableList<Solution> = mutableListOf()

// ---------------------------------------------------------------------------------

fun main() {
    println("\n*** Solving knapsack problem using genetic algorithm ***\n")
    println("Population size: $POP_SIZE, Generations: $MAX_GEN")
    println("Number of items to pick from: $chromosomeLength")
    println("Mutation threshold: $MUTATION_THRESHOLD")

    // initialize the population
    initPopulation()

    // run genetic algorithm
    runGA()

    // print the best-overall solution
    printBestSolution()
}

// ---------------------------------------------------------------------------------

fun printBestSolution() {

    bestSolutions.sortByDescending { it.fitness }
    println("\nBest solution found after $MAX_GEN generations:")

    val (chromosome, fitness) = bestSolutions[0]
    val sumWeight = (chromosome.zip(items)
            {c, item -> c * item.weight}).sum()

    println(bestSolutions[0])
    println("Sum of weights: $sumWeight   Sum of values: $fitness")
}

// ---------------------------------------------------------------------------------

fun initPopulation() {
    // initialize a population of valid solutions (of non-zero fitness)
    // each solution is represented by a chromosome

    for (person in 0 until POP_SIZE) {
        val chromosome = IntArray(chromosomeLength)

        var notDone = true
        while (notDone) {
            for (gene in 0 until chromosomeLength) {
                chromosome[gene] = (0..1).random()
            }
            val fitness = getFitness(chromosome)
            if (fitness > 0) {
                population += Solution(chromosome, fitness)
                notDone = false
            }
        }
    }
    // sort population (in-place) in descending order
    population.sortByDescending {it.fitness}

    println("\nBest solution from initial population:")
    println(population[0])
    println("\n... initPopulation done ...\n")
}

// ---------------------------------------------------------------------------------

fun getFitness(chromosome: IntArray): Int {
    // get sum of values and weights
    val sumValue  = (chromosome.zip(items) {c, item -> c * item.value}).sum()
    val sumWeight = (chromosome.zip(items) {c, item -> c * item.weight}).sum()

    return if (sumWeight <= maxWeight) sumValue else 0
}

// ---------------------------------------------------------------------------------

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

        // optional step: print the fittest solution
        printSolution(generation, population[0])
    }
}

// ---------------------------------------------------------------------------------

fun selectElites() {
    // assign top “eliteSize” individuals to nextgen
    for (i in 0 until eliteSize)
        nextgen += population[i].copy()
}

// ---------------------------------------------------------------------------------

fun tournament(): Solution {
    // random sampling with replacement
    // use the entire population including elites
    val candidate1 = population.random().copy()
    val candidate2 = population.random().copy()
    // return the winner of the tournament
    return if (candidate1.fitness >= candidate2.fitness) candidate1
            else candidate2
}

// ---------------------------------------------------------------------------------

fun crossover(parent1: Solution, parent2: Solution): Solution {
    // random single-point split and crossover
    val split = (1 until chromosomeLength).random()

    // use copyOfRange() to extract elements from an array
    // .copyOfRange(a,b): a=start index, b=not inclusive
    val arr1 = parent1.chromosome.copyOfRange(0, split)
    val arr2 = parent2.chromosome.copyOfRange(split, chromosomeLength)

    val newChromosome = arr1 + arr2

    // apply in-place mutation to the new chromosome
    mutation(newChromosome)

    return Solution(newChromosome, getFitness(newChromosome))
}

// ---------------------------------------------------------------------------------

fun mutation(newChromosome: IntArray) {
    // carry out in-place mutation
    for (i in 0 until chromosomeLength) {
        if ((0..1000).random()/1000.0 <= MUTATION_THRESHOLD) {
            // simplest way to flip values between 0 and 1 is: i=1-i
            newChromosome[i] = (1 - newChromosome[i])
        }
    }
    // nothing to return
}

// ---------------------------------------------------------------------------------

fun printSolution(generation: Int, solution: Solution) {
    val str1 = "%04d".format(generation).padEnd(10, ' ')
    val (c, f) = solution
    val str2 = c.contentToString()
    val str3 = f.toString().padStart(6, ' ')
    println(str1 + str2 + str3)
}


