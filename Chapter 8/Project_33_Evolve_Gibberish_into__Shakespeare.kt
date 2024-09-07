/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 33: Evolve Gibberish itno Shakespeare */

data class Solution (
    val chromosome: String,
    val fitness: Int
)

// global parameters

val TARGET = "To be, or not to be: that is the question."
val VALID_GENES: String =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" + // letters
            "1234567890" +                                   // numbers
            ", .-;:_!/?#%&()={[]}$*@\"\'"                    // symbols

val chromosomeLength = TARGET.length
val POP_SIZE = 100
val MAX_GEN = 1000
val ELITISM = 0.15
val eliteSize = (POP_SIZE * ELITISM).toInt()
val MUTATION_THRESHOLD = 1.0/chromosomeLength

val population: MutableList<Solution> = mutableListOf()
val nextgen: MutableList<Solution> = mutableListOf()

// ---------------------------------------------------------------------------------

fun main() {
    println("\n*** Text-matching using genetic algorithm ***\n")
    println("Target string: $TARGET")
    println("Population size: $POP_SIZE, Generations: $MAX_GEN, " +
            "Chromosome length: $chromosomeLength")
    println("Mutation threshold: $MUTATION_THRESHOLD")

    // initialize the populaiton
    initPopulation()

    // run genetic algorithm
    runGA()
}

// ---------------------------------------------------------------------------------

fun initPopulation() {
    // initialize a population of POP_SIZE individuals
    for (i in 0 until POP_SIZE) {
        var chromosome = ""
        for (j in 0 until chromosomeLength) {
            chromosome += VALID_GENES.random()
        }
        // calculate fitness of the new chromosome
        val fitness = getFitness(chromosome)
        // add the new individual to the population
        population += Solution(chromosome, fitness)
    }

    // sort population (in-place) in descending order
    population.sortByDescending {it.fitness}

    println("\nBest solution from initial population:")
    println(population[0])
    println("\n... initPopulation done ...\n")
}

// ---------------------------------------------------------------------------------

fun getFitness(chromosome: String): Int {
    var fitness = 0
    val pairs = TARGET.zip(chromosome)
    for (pair in pairs) {
        if (pair.first == pair.second)
            fitness += 1
    }
    return fitness
}

// ---------------------------------------------------------------------------------

fun runGA() {

    // iterate for a specified number of generations
    for (generation in 1 .. MAX_GEN) {

        // step 1: check for termination condition
        if (population[0].fitness >= chromosomeLength) {
            println("\n*** Target reached at generation = ${generation - 1} ***\n")
            break
        }

        // step 2: implement elitism
        selectElites()

        // step 3: implement crossover and mutation
        for (i in eliteSize until POP_SIZE) {
            // select parents for crossover
            val parent1 = tournament()
            val parent2 = tournament()

            // produce a child using crossover and mutation
            val child = crossover(parent1, parent2)

            // add child to nextgen
            nextgen += child
        }

        // step 4: transfer nextgen to current population
        for (i in nextgen.indices)
            population[i] = nextgen[i].copy()

        // step 5: clear nextgen for next iteration
        nextgen.clear()

        // step 6: sort population in descending order (in-place)
        population.sortByDescending {it.fitness}

        // optional step: print the best solution per generation
        val formatString = "%5d %44s %4d"
        println(formatString.format(generation,
            population[0].chromosome, population[0].fitness))
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
    // random single-point split crossover
    val split = (1 until chromosomeLength).random()

    // use slice to extract segments from a string
    val crossChromosome =
        parent1.chromosome.slice(0 until split) +
                parent2.chromosome.slice(split until chromosomeLength)

    // apply mutation to crossChromosome
    val newChromosome = mutation(crossChromosome)

    return Solution(newChromosome, getFitness(newChromosome))
}

// ---------------------------------------------------------------------------------

fun mutation(crossChromosome: String): String {
    // string object is immutable in Kotlin
    // create a char array whose elements can be modified
    val chars = crossChromosome.toCharArray()
    for (i in 0 until chromosomeLength) {
        if ((0..1000).random()/1000.0 <= MUTATION_THRESHOLD)
            chars[i] = VALID_GENES.random()
    }
    return String(chars)
}
