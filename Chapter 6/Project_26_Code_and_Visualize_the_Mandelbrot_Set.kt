/* KOTLIN FROM SCRATCH - Faisal Islam
   Projects 26: Code and Visualize the Mandelbrot Set

   This is the optimized version of the code that uses
   PixelWriter to change the color of the pixels of an image
   in the memory before displaying the image inside a pane.
   
*/

// import block

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

import kotlin.math.roundToInt

// global parameters for the Mandelbrot set

val xMin = -2.0
val xMax = 1.0
val yMin = -1.5
val yMax = 1.5
val increment = 0.003
val iterMax = 200

// set the image parameters
val xRange = xMax - xMin
val yRange = yMax - yMin
val imageWidth = 600
val imageHeight = ((imageWidth / xRange) * yRange).roundToInt()

/* ---------------------------------------------------------------------------------------- */

// Application class for drawing the M-set
class Mandelbrot : Application() {
    override fun start(stage: Stage) {
        val root = Pane()

        val scene = Scene(root, imageWidth.toDouble(), imageHeight.toDouble())
        scene.fill = Color.WHITE
        stage.title = "Mandelbrot Set"
        stage.scene = scene
        stage.show()

        // print global values
        println()
        println("image width = $imageWidth")
        println("xRange = $xRange\tyRange = $yRange")
        println("image height = $imageHeight")

        // search for points in the M-set and display the set
        drawMSet(root)
    }
}

fun main() {
    Application.launch(Mandelbrot::class.java)
}

/* ---------------------------------------------------------------------------------------- */

private fun drawMSet(root: Pane) {
    println()
    println("imageWidth = $imageWidth")
    println("imageHeight = $imageHeight")

    val writableImage = WritableImage(imageWidth, imageHeight)
    val pixelWriter: PixelWriter = writableImage.pixelWriter

    var y = yMin
    while (y <= yMax) {
        var x = xMin
        while (x <= xMax) {
            val cval = getConvergence(x, y)

            val hue = (cval.toDouble() / iterMax) * 360.0
            val brightness = if (cval < iterMax) 1.0 else 0.0
            val color = Color.hsb(hue, 1.0, brightness)

            val pixelX = (((x - xMin) / xRange) * (imageWidth - 1)).toInt()
            val pixelY = (((y - yMin) / yRange) * (imageHeight -1)).toInt()
            pixelWriter.setColor(pixelX, pixelY, color)

            x += increment
        }
        y += increment
    }
    // Wrap WritableImage in ImageView (a node)
    val imageView = ImageView(writableImage)
    // Add ImageView to Pane
    root.children.add(imageView)
    println("----finished calculating----")
}

/* ---------------------------------------------------------------------------------------- */

// function to check for membership in the M-set

private fun getConvergence (x: Double, y: Double): Int {
    var zx = 0.0
    var zy = 0.0
    for (i in 1..iterMax) {
        val X = zx * zx - zy * zy + x
        val Y = 2 * zx * zy + y
        if (X * X + Y * Y > 4.0) return i
        zx = X
        zy = Y
    }
    return iterMax
}