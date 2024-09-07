/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 25: Design an L-system Simulator */

// import JavaFX classes
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlin.math.*

// Global declarations
data class Rule (val key: String, val apply: String)
data class State ( val x: Double, val y: Double, val angle: Double)
val stack  = ArrayDeque <State> ()

// global parameters
val WIDTH = 600.0
val HEIGHT = 600.0

// problem definition
val axiom = "F++F++F"
val rules = listOf(
    Rule("F", "F-F++F-F"),
    //Rule("F", "FF"),
    // Rule()
)

var line =  100.0   // in pixels
val scaling = 0.33   // shrinkage factor per iteration
val ANGLE = 60.0    // in degrees
val depth = 5       // number of iterations

val turtle = Turtle(150.0, 200.0, 0.0)

var finalString = ""

/* -------------------------------------------------------------- */
/*                   The Application Class                        */
/* -------------------------------------------------------------- */

// JavaFX-Kotlin application class
class LSystemApp : Application() {
    override fun start(stage: Stage) {
        val canvas = Canvas(WIDTH, HEIGHT)
        val gc = canvas.graphicsContext2D
        // move the origin to bottom-left
        gc.translate(0.0, canvas.height)
        // let positive y-axis to point up
        gc.scale(1.0, -1.0)

        val pane = Pane()
        pane.children.add(canvas)

        val scene = Scene(pane, canvas.width, canvas.height)
        stage.title = "L-system Simulator"
        stage.scene = scene
        stage.show()

        // the L-system simulation

        finalString = axiom
        if (depth > 0) {
            for (i in 1..depth) {
                generate()
            }
            line *= (scaling).pow(depth - 1.0)
        }
        gc.lineWidth = 1.5
        draw(gc)
    }
}


fun main() {
    Application.launch(LSystemApp::class.java)
}

// -----------------------------------------------------------------------

// function to generate final L-system string
fun generate() {
    var nextString = ""
    for (letter in finalString) {
        var match = false
        for (rule in rules) {
            if (letter.toString() == rule.key) {
                match = true
                nextString += rule.apply
                break
            }
        }
        if (!match) nextString += letter
    }
    finalString = nextString
}

// -----------------------------------------------------------------------

// function to draw a sentence on canvas
fun draw (gc: GraphicsContext) {
    for (letter in finalString) {
        when (letter.toString()) {
            "F", "G" -> turtle.lineTo(line, gc)
            "J" ->      turtle.moveTo(line)
            "+" ->      turtle.turnRight(ANGLE)
            "-" ->      turtle.turnLeft(ANGLE)
            "[" ->      turtle.push()
            "]" ->      turtle.pop()
            "X" ->      { /* do nothing */  }
        }
    }
}

// -----------------------------------------------------------------------

class Turtle (private var x: Double, private var y: Double, angle: Double) {

    private var angleRad: Double = angle * PI /180

    fun lineTo(line: Double, gc: GraphicsContext) {
        val xBegin = x
        val yBegin = y
        x += line * cos(angleRad)
        y += line * sin(angleRad)
        gc.strokeLine(xBegin, yBegin, x, y)
    }

    fun moveTo(line: Double) {
        x += line * cos(angleRad)
        y += line * sin(angleRad)
    }

    fun turnRight (delta: Double) {
        // origin @ bottom-left
        angleRad += delta* PI /180
    }

    fun turnLeft (delta: Double) {
        // origin @ bottom-left
        angleRad -= delta* PI /180
    }

    fun push () {
        stack.addLast(State(x,y,angleRad))
    }

    fun pop () {
        val (xPop, yPop, anglePop) = stack.removeLast()
        x = xPop
        y = yPop
        angleRad = anglePop
    }

    fun printTurtle () {
        print("x: ${round(x*100) /100.0}  y: ${round(y * 100) /100.0}  ")
        println("angle: ${round((angleRad*180/ PI) * 100) /100.0} degrees")
    }
}