/* KOTLIN FROM SCRATCH - Faisal Islam
   Projects 26: Code and Visualize the Mandelbrot Set

   This is the optimized version of the code that uses
   PixelWriter to change the color of the pixels of an image
   in the memory before drawing the entire image in one go.

*/

// import block

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

// global parameters
val xMin = -2.0
val xMax = 1.0
val yMin = -1.5
val yMax = 1.5

val xRange = xMax - xMin
val yRange = yMax - yMin

val canvasW = 600.0
val canvasH = (canvasW/ xRange) * yRange
val increment = 0.003
val iterMax = 200

// -----------------------------------------------------------------------

// Application class for drawing the M-set
class Mandelbrot : Application() {
    override fun start(stage: Stage) {
        val root = Pane()
        val canvas = Canvas(canvasW, canvasH)
        val gc = canvas.graphicsContext2D
        root.children.add(canvas)

        val scene = Scene(root, canvasW, canvasH)
        scene.fill = Color.WHITE
        stage.title = "Mandelbrot Set"
        stage.scene = scene
        stage.show()

        // search for points in the M-set
        // and draw them on the Canvas
        drawMSet(gc)
    }
}

fun main() {
    Application.launch(Mandelbrot::class.java)
}

// -----------------------------------------------------------------------

// function to iterate over the search space and draw non-members
// using a grayscale and members as black points

private fun drawMSet(gc: GraphicsContext) {
    // initialize a writable image with specified dimensions and
    // access its pixelWrite method to directly manipulate its pixels
    val image = WritableImage(canvasW.toInt(), canvasH.toInt())
    val pixelWriter: PixelWriter = image.pixelWriter


    var y = yMin
    while (y <= yMax) {
        var x = xMin
        while (x <= xMax ) {
            val cval = getConvergence(x, y)
            val speed = cval.toDouble() / iterMax
            val factor = 1.0 -speed
            val color = Color.color(factor, factor, factor)

            val pixelX = (((x - xMin) / xRange) * canvasW).toInt()
            val pixelY = (((y - yMin) / yRange) * canvasH).toInt()
            pixelWriter.setColor(pixelX, pixelY, color)
            x += increment
        }
        y += increment
    }
    // draw the entire image from memory to the canvas
    gc.drawImage(image, 0.0, 0.0)
}

// -----------------------------------------------------------------------

// function to check for membership in the M-set

private fun getConvergence (x: Double, y: Double): Int {
    var zx = 0.0
    var zy = 0.0
    for (i in 1..iterMax) {
        val X = zx*zx - zy*zy + x
        val Y = 2*zx*zy + y
        if (X*X + Y*Y > 4.0) return i
        zx = X
        zy = Y
    }
    return iterMax
}