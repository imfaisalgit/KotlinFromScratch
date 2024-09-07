/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 13: code the Fibonacci Sequence */

// Code the Fibonacci Sequence

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.stage.Stage

// number of Fibonacci numbers in the list
val N = 9
val fibs = mutableListOf<Int>()

// canvas-related parameters
val canvasW = 1000.0
val canvasH = 750.0

// scaling parameters: adjust as needed
val xOffset = 150
val yOffset = 50
val amplify = 25.0

class FibonacciSpiral : Application() {
    override fun start(stage: Stage) {
        val root = Pane()
        val canvas = Canvas(canvasW, canvasH)
        val gc = canvas.graphicsContext2D
        gc.translate(canvas.width / 2 + xOffset,
            canvas.height / 2 + yOffset)
        root.children.add(canvas)

        val scene1 = Scene(root, canvasW, canvasH)
        scene1.fill = Color.WHITE
        with(stage) {
            title = "Fibonacci Spiral"
            scene = scene1
            show()
        }

        // code for Fibonacci sequence and spiral
        generateFibonacciNumbers()
        drawFibonacciSpiral(gc)
        printFibonacciSequenceAndRatios()
    }
}
fun main() {
    Application.launch(FibonacciSpiral::class.java)
}

fun generateFibonacciNumbers() {
    // add the starting pair
    fibs.add(0)
    fibs.add(1)

    // generate the sequence
    for (i in 2 until N) {
        fibs.add(fibs[i-1] + fibs[i-2])
    }
}

fun drawFibonacciSpiral(gc: GraphicsContext) {
    for (i in 1 until N) {
        val side = fibs[i] * amplify
        with(gc) {
            strokeRect(0.0, 0.0, side, side)
            drawText(i, gc, side)
            drawArc(gc, side)
            // move to the opposite corner by adding
            // 'side' to both x and y coordinates
            translate(side, side)
            // rotate the axes counterclockwise
            rotate(-90.0)
        }
    }
}

fun drawText(i: Int, gc: GraphicsContext, side: Double) {
    gc.fill = Color.BLACK
    with(gc) {
        font = when {
        i <= 2 -> Font.font(12.0)
        else -> Font.font(24.0)
    }
        fillText(fibs[i].toString(), side/2, side/2)
    }
}

fun drawArc(gc: GraphicsContext, side: Double) {
    val x = 0.0
    val y = -side
    with(gc) {
        lineWidth = 3.0
        strokeArc(x, y, 2*side, 2*side,
            -90.0, -90.0, ArcType.OPEN)
    }
}

private fun printFibonacciSequenceAndRatios() {
    println("\n*** Fibonacci sequence and ratios ***\n")
    println("Length of Fibonacci sequence=${fibs.size}")
    println("Generated sequence:")
    println(fibs)
    println("\nRatio F(n+1)/F(n) [starting from (1,1) pair]:")
    for (i in 2 until fibs.size) {
        println("%5d".format(fibs[i-1]) +
                "%5d".format(fibs[i]) +
                "%12.6f".format(fibs[i].toDouble()/fibs[i-1])
        )
    }
}
