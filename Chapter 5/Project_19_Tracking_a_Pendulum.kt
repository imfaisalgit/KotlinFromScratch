/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 05: Projects 19 */

// Creating patterns with water jet

// import block
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.chart.*
import javafx.stage.Stage
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

// data classes
data class XYChartData (val x: Double, val y: Double )
data class PendulumData (val theta: Double, val omega: Double,
                         val t: Double)

// problem definition and global parameters
val theta0 = -PI/6  // angular displacement at t=0, rad
val omega0 = 0.0    // angular velocity at t=0, rad/s
val l = 0.4         // length, m
val g = 9.81        // acceleration due to gravity, m/s2
val n = 100         // intervals
val gamma = 0.6     // drag coefficient

class SimplePendulum : Application() {
    override fun start(primaryStage: Stage) {
        val root = VBox()
        val scroll = ScrollPane()
        scroll.content = root
        val scene = Scene(scroll, 550.0, 600.0, Color.WHITE)
        primaryStage.title = "Simple Pendulum"
        primaryStage.scene = scene
        primaryStage.show()

        // generate pendulum state data
        val state: List<PendulumData> =
            simplePendulumWithDrag(theta0, omega0,
                l, g, n, gamma)

        // create (x, y) series for plotting
        val list1 = mutableListOf<XYChartData>()
        val list2 = mutableListOf<XYChartData>()
        val list3 = mutableListOf<XYChartData>()

        for (item in state) {
            val (theta, omega, t) = item
            list1 += XYChartData(t, theta)  // t along x-axis
            list2 += XYChartData(t, omega)  // t along x-axis
            list3 += XYChartData(theta, omega)
        }

        // call singleXYChart() to generate plots
        val xyChart1 =
            singleXYChart(list1,
                title = "Angular Displacement Over Time",
                xLabel = "Time (sec)",
                yLabel = "Angular displacement (rad)")
        val xyChart2 =
            singleXYChart(list2,
                title = "Angular Velocity Over Time",
                xLabel = "Time (sec)",
                yLabel = "Angular velocity (rad/sec)")
        val xyChart3 =
            singleXYChart(list3,
                title = "Phase-Space Plot (omega vs. theta)",
                xLabel = "Angular displacement (rad)",
                yLabel = "Angular velocity (rad/sec)",
                sort = "NONE")

        // add the charts to the root (VBox) object
        root.children.addAll(xyChart1, xyChart2, xyChart3)
    }
}

fun main() {
    Application.launch(SimplePendulum::class.java)
}

// ------------------------------------------------------------------------

fun simplePendulumWithDrag (
    theta0 : Double,
    omega0 : Double,
    l: Double, g: Double, n: Int,
    gamma: Double = 0.0): List<PendulumData> {

    // set local variables, parameters and list
    val alpha = g / l
    // calculate period for small displacement
    val T = 2 * PI * sqrt(l/g)
    val dt = T / n
    val Nmax = 4 * n
    val df = DecimalFormat("##.####")

    var omegaOld: Double; var omegaNew: Double
    var thetaOld: Double; var thetaNew: Double
    var timeOld: Double;  var timeNew: Double
    val pList = mutableListOf<PendulumData>()

    // initialize for t=0
    thetaOld = theta0
    omegaOld = omega0
    timeOld = 0.0
    pList += PendulumData(theta0, omega0, 0.0)

    // calculate and save state variables
    for (k in 1..Nmax) {
        omegaNew = omegaOld - (alpha * sin(thetaOld) + gamma * omegaOld) * dt
        thetaNew = thetaOld + omegaNew * dt // Euler-Cromer
        timeNew = timeOld + dt
        pList += PendulumData(thetaNew, omegaNew, timeNew)

        omegaOld = omegaNew
        thetaOld = thetaNew
        timeOld = timeNew
    }

    println("\n*** Simple Pendulum Simulation ***\n")
    println("length l: $l m")
    println("theta0: ${df.format(theta0*180/PI)} degrees")
    println("omega0: ${df.format(omega0*180/PI)} rad/sec")
    println("gamma: ${df.format(gamma)}")
    println("dt: ${df.format(dt)} sec")
    println("Nmax: $Nmax intervals")
    println("Simulation length: ${df.format(Nmax*dt)} sec")

    return pList
}

// ------------------------------------------------------------------------

fun singleXYChart(data: List<XYChartData>,
                  title: String  = "",
                  xLabel: String = "x-axis",
                  yLabel: String = "y-axis",
                  sort: String = "default"): LineChart<Number, Number> {

    // define axes
    val xAxis = NumberAxis()
    val yAxis = NumberAxis()
    xAxis.label = xLabel
    yAxis.label = yLabel

    // create LineChart
    val lineChart = LineChart(xAxis, yAxis)
    lineChart.title = title
    lineChart.createSymbols = false
    lineChart.isLegendVisible = false

    if (sort == "NONE")
        lineChart.axisSortingPolicy = LineChart.SortingPolicy.NONE

    // define series
    val series = XYChart.Series<Number, Number>()

    // populate series with data
    for (item in data) {
        val (x, y) = item
        series.data.add(XYChart.Data(x, y))
    }

    // assign series with data to LineChart
    lineChart.data.add(series)

    // return LineChart object
    return lineChart
}