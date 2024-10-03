/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 24: Create a Fractal Tree */

// import JavaFX classes
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.stage.Stage

// import Kotlin math methods
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

// -----------------------------------------------------------------------

class FractalTree : Application() {
    override fun start(stage: Stage) {

        val canvas = Canvas(1000.0, 800.0)
        val gc = canvas.graphicsContext2D
        val rootNode = FlowPane()
        rootNode.alignment = Pos.CENTER

        // add the canvas and button to the scene graph.
        rootNode.children.addAll(canvas)


        val scene = Scene(rootNode, canvas.width, canvas.height)
        stage.title = "Fractal tree"
        stage.scene = scene
        stage.show()

        // this block is for fractal (binary) tree
        // ---------------------------------------
        val x = canvas.width/2.0
        val y = canvas.height - 100
        val angle = -PI/2
        val len = 60.0
        var phi = PI/10.0

        gc.stroke = Color.GRAY
        drawTree(x, y, angle, len, phi, gc)
    }
}

// -----------------------------------------------------------------------

fun main() {
    Application.launch(FractalTree::class.java)
}

// -----------------------------------------------------------------------

private fun drawTree(x1: Double, y1: Double, theta: Double,
                     len: Double, phi: Double,
                     gc: GraphicsContext) {
    if (len > 10) {
        var x2 = x1 + len * cos(theta)
        var y2 = y1 + len * sin(theta)
        //gc.stroke = Color.WHITE
        gc.strokeLine(x1, y1, x2, y2)

        drawTree(x2, y2, theta + phi, len - 4, phi, gc)
        drawTree(x2, y2, theta - phi, len - 4, phi, gc)

    } else {
        gc.fill = Color.BLACK
        gc.fillOval(x1-2, y1-2, 4.0, 4.0)
    }
}
