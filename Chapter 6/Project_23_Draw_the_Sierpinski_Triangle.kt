/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 23: Draw the Sierpinski Triangle */

// import JavaFX classes
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.FlowPane
import javafx.stage.Stage

// import Kotlin math methods
import kotlin.math.sin
import kotlin.math.PI
import kotlin.math.pow

// global parameters
val BASE = 500.0
val DEPTH = 7
val baseMin = BASE * ((0.5).pow(DEPTH))

data class Vertices(var p1: Double, var q1: Double,
                    var p2: Double, var q2: Double,
                    var p3: Double, var q3: Double)

// ----------------------------------------------------------------
class HelloApplication : Application() {
    override fun start(stage: Stage) {

        val rootNode = FlowPane()
        rootNode.alignment = Pos.CENTER
        val canvas = Canvas(600.0, 600.0)
        val gc = canvas.graphicsContext2D

        // Add the canvas and button to the scene graph.
        rootNode.children.addAll(canvas)

        val scene = Scene(rootNode,600.0, 600.0)
        stage.title = "Sierpinski Triangle"
        stage.scene = scene
        stage.show()

        // initialize and call function to draw Sierpinski Triangle
        val b = BASE      // BASE set to 500.0 globally
        val h = b * sin(PI/3.0)
        val x1 = 300.0
        val y1 = 50.0

        val x = doubleArrayOf(x1, x1-b/2, x1+b/2)
        val y = doubleArrayOf(y1, y1+h, y1+h)
        // draw the outermost triangle
        gc.strokePolygon(x, y, 3)
        // call the recursive function
        drawTriangle(x1, y1, b, h, gc)
    }
}

// ----------------------------------------------------------------

fun main() {
    Application.launch(HelloApplication::class.java)
}

// ----------------------------------------------------------------

fun drawTriangle(x1: Double, y1: Double, base: Double,
                 height: Double, gc: GraphicsContext) {

    if (base > baseMin) {
        val (p1, q1, p2, q2, p3, q3) = getVertices(x1, y1, base, height)

        val p = doubleArrayOf(p1, p2, p3)
        val q = doubleArrayOf(q1, q2, q3)
        gc.strokePolygon(p, q, 3)

        // recurse for non-empty child triangles
        drawTriangle(x1, y1, base/2, height/2, gc)
        drawTriangle(p1, q1, base/2, height/2, gc)
        drawTriangle(p3, q3, base/2, height/2, gc)
    }
}

// ----------------------------------------------------------------

fun getVertices(x1: Double, y1: Double, base: Double, height: Double) : Vertices {

    return Vertices(x1-base/4, y1 + height/2, x1,y1+height,
        x1+base/4, y1 + height/2)
}