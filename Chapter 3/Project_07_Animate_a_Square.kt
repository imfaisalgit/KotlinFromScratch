/* KOTLIN FROM SCRATCH - Faisal Islam */
/* PROJECT 7: Animate a Square */

// graphics related imports
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

// animation related imports
import javafx.animation.Transition
import javafx.animation.TranslateTransition
import javafx.util.Duration

class TransitionExample : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Transition Example"

        // create a square
        val square = Rectangle(50.0, 50.0, Color.RED)
        square.y = 100.0
        // create a pane to hold the square
        val pane = Pane(square)

        // create a scene and show the stage
        val scene = Scene(pane, 300.0, 300.0)
        primaryStage.scene = scene
        primaryStage.show()

        // create a TranslateTransition class instance
        // and set its properties
        val transition =
            TranslateTransition(Duration.seconds(2.0), square)

        with (transition) {
            fromX = 0.0
            toX = pane.width - square.width
            cycleCount = Transition.INDEFINITE
            isAutoReverse = true
            play()
        }
    }
}

fun main() {
    Application.launch(TransitionExample::class.java)
}