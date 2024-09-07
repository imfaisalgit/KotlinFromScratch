/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Chapter 01: full examples */

// -------------------- Listing 01 --------------------

fun main() {
    val name = "John Sinclair"
    val age = 30
    println("$name is $age years old")
}

// -------------------- Listing 02 --------------------

fun main() {
    var name = "John Sinclair"
    var age = 30
    println("$name is $age years old")
    name = "John Sinclair Jr."
    age = 12
    println("$name is $age years old")
}

// -------------------- Listing 03 --------------------

fun main() {
    
    // example without parentheses
    val resultWithoutParentheses = 5 + 3 * 2    
    println("Result without parentheses: $resultWithoutParentheses")

    // example with parentheses
    val resultWithParentheses = (5 + 3) * 2
    println("Result with parentheses: $resultWithParentheses") 
}

// -------------------- Listing 04 --------------------

fun main() {
    val name = "John"
    val age = 30

    // using escape characters in string template
    val message = "Name: $name\nAge: $age"

    println(message)
}

// -------------------- Listing 05 --------------------

fun main() {
    val a = 100
    val b = -30
    val max: Int

    if (a > b) {
        max = a
        println("a is greater than b.")
        println("max of $a and $b is: $max")
    } else if (a < b) {
        max = b
        println("b is greater than a.")
        println("max of $a and $b is: $max")
    } else
        println("a and b have the same value: $a")
}

// -------------------- Listing 06 --------------------

fun main() {
    val x = 5

    when {
        x > 0 -> println("x is positive")
        x == 0 -> println("x is zero")
        x < 0 -> println("x is negative")
        else -> println("x is not a real number")
    }
}

// -------------------- Listing 07 --------------------

fun main() {
    val hour = 13
    when (hour) {
        in 0..11 -> println("Good morning")
        in 12..16 -> println("Good afternoon")
        in 17..23 -> println("Good evening")
        else -> println("Invalid hour")
    }
}

// -------------------- Listing 08 --------------------

fun main() {
    val size = 4 // change this to adjust the size of the square

    // nested for loop to print a square pattern of asterisks
    for (i in 1..size) {
        for (j in 1..size) {
            print("* ")
        }
        println() // Move to the next line after each row
    }
}

// -------------------- Listing 09 --------------------

import kotlin.math.sqrt
import kotlin.math.pow

fun main() {
   val x = 100.0
   val y = 10.0
 
   val squareRoot = "The square root of $x is: ${sqrt(x)}"
   val toThePower2 = "$y raised to the 2nd power is: ${y.pow(2.0)}"

   println(squareRoot)
   println(toThePower2)
}

// -------------------- Listing 10 --------------------

fun add(x: Int, y: Int): Int {
    return x + y
}

fun main(){
    // declare the variables
    val a = 3
    val b = 6
    
    // call the function to add two integers
    val sum = add(a, b)
    println("The sum of $a and $b is $sum.")
}

// -------------------- Listing 11 --------------------


fun add(x: Double, y: Double): Double {
    return x + y
}
fun multiply(x: Double, y: Double): Double {
    return x * y
}

// change this condition to use add() or multiply()
val useAdd = true

fun main() {
    // declare a function variable using member reference
    val selectedFunction = if (useAdd) ::add else ::multiply

    val x = 3.0
    val y = 4.0

    // calculate the value of the selected function
    val result = selectedFunction(x, y)

    // print the result
    println("Result: $result")
}

// -------------------- Listing 12 --------------------

fun printMessage(message: String) {
    println(message)
}

fun applyFunction(function: (String) -> Unit, input: String) {
    function(input)
}

fun main() {
    // using :: to reference the printMessage function
    applyFunction(::printMessage, "Hello, Kotlin!")
}

// -------------------- Listing 13 --------------------

fun main() {

    while (true) {
        print("Enter an integer: ")
        val num = readln()

        // validate using try…catch block
        try {
            val intValue = num.toInt()
            println("You entered: $intValue")
            break // stop the loop on success
        } catch (e: NumberFormatException) {
            println("Invalid input. Try again.")
        }
    }
}

// -------------------- Listing 14 --------------------

import java.io.File
import java.util.Scanner

fun main() {

    // replace the path below with the path to your file
    val inputFile = "inputfile.txt" 

    try {
        val file = File(inputFile)
        val sc = Scanner(file)
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            println(line)
        }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
}


// -------------------- Listing 15 --------------------

import java.io.File

fun main() {
    // replace the file locations as needed
    val inputFile = File("inputfile.txt")
    val outputFile = File("outputfile.txt")

    // read all lines from the input file
    val lines = inputFile.readLines()

    // write all lines to the output file
    for (line in lines) {
        outputFile.appendText("$line\n")
    }
    println("Copied input_file.txt to output_file.txt")
}


