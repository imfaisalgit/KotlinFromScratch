Predict the Flight of a Cannonball
/* Projects 18: Design a Fountain with Water Jets */

// import JavaFX classes
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

// import required math functions
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.PI
import kotlin.math.sqrt

// set global parameters/variables
val baseWidth = 6.5  // m
val xMaxJet = doubleArrayOf(2.25, 2.55, 2.85, 3.0)
val yMaxJet = doubleArrayOf(1.5, 3.0, 4.25, 5.5)
val angle = DoubleArray(4)
val vel = DoubleArray(4)
val g = 9.8  // m/s2

// set canvas properties
val xMin = -0.5
val xMax = baseWidth + 0.5
val yMin = -0.5
val yMax = 6.0
val xRange = xMax - xMin
val yRange = yMax - yMin
val canvasW = 700.0
val canvasH = (canvasW/ xRange) * yRange
val scaleX = canvasW / xRange
val scaleY = canvasH / yRange

// ------------------------------------------------------------------------

// the primary application class
class ShapeOfWater : Application() {
    override fun start(stage: Stage) {
        val root = Pane()
        val canvas = Canvas(canvasW, canvasH)
        val gc = canvas.graphicsContext2D
        gc.translate(0.0, canvas.height)
        gc.scale(1.0, -1.0)
        root.children.add(canvas)
        val scene = Scene(root, canvasW, canvasH)
        scene.fill = Color.WHITE
        stage.title = "Shape of Water"
        stage.scene = scene
        stage.show()

        // problem specific section
        println("\n***   Calculate the water trajectories " +
                "and plot them with the fountain   ***\n")

        // calculate the water trajectories
        getAngleAndVel()
        getTrajectories(gc)

        // draw the fountain and nozzles
        drawFountain(gc)
        drawNozzles(gc)

        println("\nNozzle velocities:")
        for (v in vel) print(String.format("%.2f  ", v))
        println("\nNozzle angles:")
        for (theta in angle)
            print(String.format("%.2f  ", theta))
    }
}

fun main() {
    Application.launch(ShapeOfWater::class.java)
}

// ------------------------------------------------------------------------

fun getAngleAndVel() {
    var index = 0

    xMaxJet.zip(yMaxJet) { x, y ->
        val theta = atan(2 * y / x)
        angle[index] = theta * 180/ PI
        vel[index] = sqrt(2 * g * y) / sin(theta)
        index += 1
    }
}

// ------------------------------------------------------------------------

fun getTrajectories(gc: GraphicsContext) {
    // generate trajectories by iterating over time
    vel.zip(angle) {v, _theta ->
        val theta = _theta * PI / 180
        val tmax = 1.1 * v * sin(theta)/g
        val delt = tmax/50

        // calculate trajectory coordinates
        var t = 0.0
        while (t <= tmax) {
            val x = v * cos(theta) * t
            val y = x * tan(theta) -
                    (g / (2 * (v * cos(theta)).pow(2))) * x.pow(2)

            // draw points on canvas
            gc.fillOval(canvasW * (x - xMin)/ xRange,
                canvasH * (y - yMin)/ yRange, 3.0, 3.0)
            gc.fillOval(
                canvasW * ((baseWidth - x) - xMin)/ xRange,
                canvasH * (y - yMin)/ yRange, 3.0, 3.0)
            t += delt
        }
    }
}

// ------------------------------------------------------------------------

fun drawFountain(gc: GraphicsContext) {

    // set graphics context properties
    with (gc) {
        //fill = Color.LIGHTGRAY
        stroke = Color.BLACK
        lineWidth = 7.0
    }

    // set scale properties

    val yMargin = doubleArrayOf(0.95, 0.97, 0.98, 0.99)
    val columnHeightFactor = doubleArrayOf(0.67, 0.67, 0.67, 0.67)

    for (i in xMaxJet.indices) {

        val level = i + 1

        // there are 4 unique x values and 3 unique y values per level
        var x1 = 0.0; var x2= 0.0; var x3 = 0.0; var x4 = 0.0
        var y1 = 0.0; var y2= 0.0; var y3 = 0.0

        when(level) {
            1 -> {
                println("--- drawing level: $level ---")

                x1 = baseWidth/4 + xMaxJet[i]/2
                x2 = baseWidth - x1
                x3 = xMaxJet[i]
                x4 = baseWidth - xMaxJet[i]

                y3 = yMaxJet[i] * yMargin[i]
                y1 = 0.0
                y2 = y1 + (y3 - y1) * columnHeightFactor[0]

                drawFountainLevels(gc, x1, x2, x3, x4, y1, y2, y3)
            }
            2 -> {
                println("--- drawing level: $level ---")

                x1 = baseWidth/4 + xMaxJet[i]/2
                x2 = baseWidth - x1
                x3 = xMaxJet[i]
                x4 = baseWidth - xMaxJet[i]

                y3 = yMaxJet[i] * yMargin[i]
                y1 = yMaxJet[i - 1] * yMargin[i-1]
                y2 = y1 + (y3 - y1) * columnHeightFactor[i]

                drawFountainLevels(gc, x1, x2, x3, x4, y1, y2, y3)
            }
            3 -> {
                println("--- drawing level: $level ---")

                x1 = baseWidth/4 + xMaxJet[i]/2
                x2 = baseWidth - x1
                x3 = xMaxJet[i]
                x4 = baseWidth - xMaxJet[i]

                y3 = yMaxJet[i] * yMargin[i]
                y1 = yMaxJet[i - 1] * yMargin[i-1]
                y2 = y1 + (y3 - y1) * columnHeightFactor[i]

                drawFountainLevels(gc, x1, x2, x3, x4, y1, y2, y3)
            }
            4 -> {
                println("--- drawing level: $level ---")

                x1 = baseWidth/4 + xMaxJet[i]/2
                x2 = baseWidth - x1
                x3 = xMaxJet[i]
                x4 = baseWidth - xMaxJet[i]

                y3 = yMaxJet[i] * yMargin[i]
                y1 = yMaxJet[i - 1] * yMargin[i-1]
                y2 = y1 + (y3 - y1) * columnHeightFactor[i]

                drawFountainLevels(gc, x1, x2, x3, x4, y1, y2, y3)

            }
            else ->
                println("invalid level specified")

        }
    }
}

// ------------------------------------------------------------------------

fun drawFountainLevels(gc: GraphicsContext, x1: Double, x2: Double, x3: Double, x4: Double,
                       y1: Double, y2: Double, y3: Double) {

    // draw upper rectangle
    val xArrayU = doubleArrayOf(x3, x4, x4, x3)
        .map { (it - xMin) * scaleX }.toDoubleArray()
    val yArrayU = doubleArrayOf(y2, y2, y3, y3)
        .map { (it - yMin) * scaleY}.toDoubleArray()

    gc.strokePolygon(xArrayU, yArrayU, 4)

    // draw lower rectangle
    val xArrayL = doubleArrayOf(x1, x2, x2, x1)
        .map { (it - xMin) * scaleX }.toDoubleArray()
    val yArrayL = doubleArrayOf(y1, y1, y2, y2)
        .map { (it - yMin) * scaleY}.toDoubleArray()

    gc.strokePolygon(xArrayL, yArrayL, 4)
}

// ------------------------------------------------------------------------

fun drawNozzles(gc: GraphicsContext) {
    gc.fill = Color.BLACK
    // upper left x and y, width, height
    gc.strokeRect(-xMin * scaleX -2, -yMin * scaleY,2.0, 2.0)
    gc.strokeRect((baseWidth - xMin) * scaleX, -yMin * scaleY,
        2.0, 2.0)
}

// ------------------------------------------------------------------------