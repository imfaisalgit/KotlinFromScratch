/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 22: The "Hello, world!" of Fractals */

// import JavaFX classes
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.stage.Stage

// global variables 
val ITER_MAX = 22
var iter = 1

// beginning of the Application class
class GeometricFractal : Application() {
    override fun start(stage: Stage) {

        val canvas = Canvas(600.0, 600.0)
        val gc = canvas.graphicsContext2D

        val rootNode = FlowPane()
        rootNode.alignment = Pos.CENTER
        rootNode.children.add(canvas)

        val scene = Scene(rootNode, 600.0, 600.0)
        stage.title = "Geometric Fractal"
        stage.scene = scene
        stage.show()

        // problem specific code segment
        val x = 50.0
        val y = 50.0
        val s = 400.0
        val k = 0.15
        gc.fill = Color.BLACK
        
        gc.strokePolygon(doubleArrayOf(x, x, x + s, x + s),
            doubleArrayOf(y, y + s, y + s, y), 4)

        drawSquares(x, y, s, k, gc)
    }
}

fun main() {
    Application.launch(GeometricFractal::class.java)
}

// ----------------------------------------------------------------

fun drawSquares(_x: Double, _y: Double, _s: Double,
                k: Double, gc: GraphicsContext) {
    
    if (iter <= ITER_MAX) {
        val d = 0.5 * _s * k
        val s = _s - 2 * d
        val x = _x + d
        val y = _y + d
        gc.strokePolygon(
            doubleArrayOf(x, x, x + s, x + s),
            doubleArrayOf(y, y + s, y + s, y), 4)
        
        // update counter
        iter += 1
        // recursive call
        drawSquares(x, y, s, k, gc)
    }
}

