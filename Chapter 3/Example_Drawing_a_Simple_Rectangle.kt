/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Example: Drawing a Simple Rectangle on Canvas */


import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

class CanvasExample_1 : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Canvas Example"

        val canvas = Canvas(400.0, 200.0)
        val gc = canvas.getGraphicsContext2D()
        val pane = Pane(canvas)
        val scene = Scene(pane)
        primaryStage.setScene(scene)
        primaryStage.show()

        drawRectangle(gc)
    }
    fun drawRectangle(gc: GraphicsContext) {
        with(gc) {
            stroke = Color.RED
            strokeRect(100.0, 50.0, 200.0, 100.0)
        }
    }
}

fun main() {
    Application.launch(CanvasExample_1::class.java)
}