/* KOTLIN FROM SCRATCH - Faisal Islam */
/* PROJECT 3: Build "Hello, World!" in JavaFX */

import javafx.application.Application
import javafx.geometry.Pos
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text

// create the JavaFX application calss

class HelloWorld : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Primary Stage"

        val text = Text("Hello, world!")
        text.font = Font.font("Verdana", 20.0)

        val vbx = VBox(text)
        vbx.alignment = Pos.CENTER
        val scene = Scene(vbx, 300.0, 300.0)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

// launch the application from the main() fucntion

fun main() {
    Application.launch(HelloWorld::class.java)
}