/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 05: Projects 21 */

// Simulate a Binary Star system

// animation related tools
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.util.Duration

// graphics related tools
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

// math functions
import kotlin.math.*

// data class
data class Star(
    val mass: Double,
    val size: Double,
    var x: Double,
    var y: Double,
    var vx: Double,
    var vy: Double,
    var xOld: Double = 0.0,
    var yOld: Double = 0.0,
    var trailCount: Int = 0,
    val color: Color = Color.GOLD
)

// problem definition and global declarations
// initial state of the binary system
val stars = listOf(
    Star(mass=0.73606507, size=40.0, x=-35.0, y=0.0,
        vx=0.0,vy=0.3045865, color= Color.BLACK),
    Star(mass=0.50121162, size=25.0, x=51.4, y=0.0,
        vx=0.0,vy=-0.447307098, color=Color.BLACK)
)
val G = 4 * PI * PI

// set canvas/animation parameters
val canvasW = 800.0
val canvasH = 800.0
val durationMillis = 4.0
val frameCountMax = 50_000

// parameters related to star trails
val TRAIL_CODE = "YES"
val trails = Array(2) { ArrayList<Pair<Double,Double>>() }
val trailMAX = 6500
val trailSize = 2.0
val scaleFactor = 4

class SimulateBinarySystem : Application() {
    override fun start(stage: Stage) {
        val root = Pane()
        val canvas = Canvas(canvasW, canvasH)
        val gc = canvas.graphicsContext2D
        gc.translate((canvas.width)/2.0, (canvas.height)/2.0)
        gc.scale(1.0, -1.0)
        root.children.add(canvas)

        val scene = Scene(root, canvasW, canvasH)
        //scene.fill = Color.WHITE
        stage.title = "Binary System Simulation"
        stage.scene = scene
        stage.show()

        // -----------simulation block-----------

        // set the background and initial positions
        initialPositions(gc)

        // start animation
        val t = Timeline()
        var frameCount = 0
        val dt = 1.0
        val iterMax = 1

        val k = KeyFrame(Duration.millis(durationMillis), {
            for (i in 1..iterMax)
                updateStarPositions(stars, dt)

            drawStars(gc)

            if (TRAIL_CODE == "YES")
                updateAndDrawTrails(gc)

            frameCount += 1
            // check the stopping condition
            if (frameCount >= frameCountMax) {
                println("maximum limit for frameCount reached")
                t.stop()
            }
        })

        t.keyFrames.add(k)
        t.cycleCount = Timeline.INDEFINITE
        t.play()
    }
}


fun main() {
    Application.launch(SimulateBinarySystem::class.java)
}

// ------------------------------------------------------------------------

fun initialPositions(gc: GraphicsContext) {
    drawAxes(gc)
    stars.forEachIndexed {index, star ->
        gc.fill = star.color
        gc.fillOval(
            scaleFactor * star.x - star.size/2,
            scaleFactor * star.y - star.size/2,
            star.size, star.size)

        // place the tracers to initial star position
        if (TRAIL_CODE == "YES") {
            for (i in 1..trailMAX) {
                trails[index].add(Pair(star.x, star.y))
            }
        }
    }
}

// ------------------------------------------------------------------------

fun drawAxes(gc: GraphicsContext) {
    // draw the x and y axes
    with(gc) {
        setLineDashes()
        lineWidth = 0.25
        stroke = Color.BLACK
        strokeLine(-canvasW/2,0.0, canvasW/2,0.0)
        strokeLine(0.0,-canvasH/2, 0.0,canvasH/2)
    }
}

// ------------------------------------------------------------------------

fun updateStarPositions(stars: List<Star>, dt: Double) {

    val rx = stars[1].x - stars[0].x
    val ry = stars[1].y - stars[0].y
    val r = sqrt(rx*rx + ry*ry )

    val force =
        G * stars[0].mass * stars[1].mass / (r * r)

    var sign = 1
    for (star in stars) {
        // update the acceleration, velocity and position of stars
        val acceleration = force / star.mass
        val ax = acceleration * rx / r
        val ay = acceleration * ry / r
        star.vx += sign * ax * dt
        star.vy += sign * ay * dt

        // these will be needed for updating trails
        star.xOld = star.x
        star.yOld = star.y

        star.x += star.vx * dt
        star.y += star.vy * dt
        sign = -1
    }
}

// ------------------------------------------------------------------------

fun drawStars(gc: GraphicsContext) {
    gc.clearRect(-canvasW/2, -canvasH/2,
        canvasW, canvasH)

    drawAxes(gc)

    // connect the centers of the stars
    with (gc) {
        lineWidth = 0.5
        stroke = Color.BLACK
        setLineDashes(2.0,4.0,4.0,2.0)
        strokeLine(
            scaleFactor*stars[0].x,
            scaleFactor*stars[0].y,
            scaleFactor*stars[1].x,
            scaleFactor*stars[1].y)
    }

    // draw the stars using updated positions
    for (star in stars) {
        gc.fill = star.color
        gc.fillOval(
            scaleFactor * star.x - star.size/2,
            scaleFactor * star.y - star.size/2,
            star.size, star.size)
    }
}

// ------------------------------------------------------------------------

fun updateAndDrawTrails(gc: GraphicsContext) {
    // update the trails
    stars.forEachIndexed { index, star ->
        if (star.trailCount >= trailMAX) star.trailCount = 0
        trails[index][star.trailCount] =
            Pair(star.xOld, star.yOld)
        star.trailCount += 1
    }

    // draw the trails
    trails.forEachIndexed { index, trail ->
        gc.fill = stars[index].color
        for (point in trail) {
            gc.fillOval(
                scaleFactor * point.first - trailSize / 2,
                scaleFactor * point.second - trailSize / 2,
                trailSize, trailSize
            )
        }
    }
}
