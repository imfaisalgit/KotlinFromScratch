/* KOTLIN FROM SCRATCH - Faisal Islam */
/* PROJECT 1: Build a Console-Based Calculator */

import kotlin.system.exitProcess

fun main() {

    println("***  Console Calculator  ***")

    // step 1: input collection
    println("\nEnter two numbers:\n")
    val number1 = readDoubleInput("Number 1: ")
    val number2 = readDoubleInput("Number 2: ")

    // step 2: operation selection
    showChoices()
    val operation = getArithmeticOperation()

    // step 3: calculation
    val result = performCalculation(number1, number2, operation)

    // step 4: result display
    println("\nResult:\n" +
            "$number1 $operation $number2 = $result")
}

// --------------------------------------------------------------------
// step 1
fun readDoubleInput(prompt: String): Double {
    print(prompt)
    val num = readln()
    // check input validity
    try {
        return num.toDouble()
    } catch (e: Exception) {
        println("Error reading input: ${e.message}")
        exitProcess(1)
    }
}

// --------------------------------------------------------------------

fun showChoices() {
    println("\nOperation Options:")
    println("1. Addition (+)")
    println("2. Subtraction (-)")
    println("3. Multiplication (*)")
    println("4. Division (/)")
}

// --------------------------------------------------------------------
// step 2
fun getArithmeticOperation(): String {
    print("\nEnter an arithmetic operation (+, -, *, /): ")
    val operation = readln()

    if(!"+-*/".contains(operation, true)){
        println("\nInvalid operation. Exiting.")
        exitProcess(2) // exit the program
    }
    return operation
}

// --------------------------------------------------------------------
// step 3
fun performCalculation(number1: Double, number2: Double,
                       operation: String): Double {

    return when (operation) {
        "+" -> number1 + number2
        "-" -> number1 - number2
        "*" -> number1 * number2
        "/" -> if (number2 != 0.0) number1 / number2
               else {
                    println("\nDivision by zero is not allowed. Exiting.")
                    exitProcess(3)
               }
        else -> {
            println("\nUnexpected error encountered. Exiting.")
            exitProcess(4)
        }
    }
}