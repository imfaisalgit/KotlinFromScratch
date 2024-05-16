/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 04: Projects 16 */

// 1D random walk simulation

// import block
import javafx.application.Application
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.geometry.Insets
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.sqrt

// data class
data class State(
    var step: Double,
    var dist: Double
)

// global parameters
val numStep = 1000
val numSim = 500

// create lists needed for plotting line charts
val xList  : List<State> = List(numStep) { State(0.0, 0.0) }
val avgList: List<State> = List(numStep) { State(0.0, 0.0) }
val rmsList: List<State> = List(numStep) { State(0.0, 0.0) }
val expList: List<State> = List(numStep) { State(0.0, 0.0) }

val states1 = mutableListOf<List<State>>()
val states2 = mutableListOf<List<State>>()

class RandomWalk1D : Application() {
    override fun start(primaryStage: Stage) {

        val root = VBox()
        /*------------------------------------------*/
        root.styleClass.add("color-palette")
        root.background = Background(BackgroundFill(Color.WHITE,
            CornerRadii.EMPTY, Insets.EMPTY))
        /*------------------------------------------*/
        val scroll = ScrollPane()
        scroll.content = root

        val scene = Scene(scroll, 550.0, 850.0, Color.WHITE)
        primaryStage.title = "1D random Walk Simulation"
        primaryStage.scene = scene
        primaryStage.show()

        // ----- random walk simulation starts here -----

        // call random walk function
        randomWalk1d()

        // get the theoretical RMS values
        calcRMS1d()

        // create linecharts
        createRWChart1(root)
        createRWChart2(root)
    }
}
fun main() {
    Application.launch(RandomWalk1D::class.java)
}

// ---------------------------------------------------------------------------

fun randomWalk1d() {

    // create local arrays
    val s = Array (numSim) {DoubleArray(numStep)}
    val sumX = DoubleArray(numStep)
    val sumX2 = DoubleArray(numStep)

    // walk "numStep" steps "numSim" times
    for (i in 0 until numSim) {
        var draw: Int
        var step: Int
        for (j in 1 until numStep) {
            draw = (0..1).random()
            step = if (draw == 0) -1 else 1
            s[i][j] = s[i][j-1] + step
            sumX[j] += s[i][j]
            sumX2[j] += (s[i][j] * s[i][j])
            xList[j].step = j.toDouble()
            xList[j].dist = s[i][j]
        }
        states1.add(xList.map {it.copy()})
    }

    // create average (mean) and RMS for distances traveled
    for (j in 0 until numStep) {
        avgList[j].step = j.toDouble()
        avgList[j].dist = sumX[j] / numSim
        rmsList[j].step = j.toDouble()
        rmsList[j].dist = sqrt(sumX2[j] / numSim)
    }
    states2.addAll(listOf(avgList, rmsList))
}

// ---------------------------------------------------------------------------

fun calcRMS1d() {
    // create the theoretical (exponential) rms/list
    for (j in 0 until numStep) {
        expList[j].step = j.toDouble()
        expList[j].dist = sqrt(j.toDouble())
    }
    states2.add(expList)
}

// ---------------------------------------------------------------------------

fun createRWChart1(root: VBox) {
    val xyChart1 =
        singleXYChart(states1,
            title = "Random Walk 1D Experiment",
            xLabel = "Steps",
            yLabel = "Cumulative distance traveled")
    root.children.add(xyChart1)
}

fun createRWChart2(root: VBox) {
    val xyChart2 =
        singleXYChart(states2,
            title = "Random Walk 1D Experiment",
            xLabel = "Steps",
            yLabel = "Mean and RMS distance traveled")
    root.children.add(xyChart2)
}

// ---------------------------------------------------------------------------

fun singleXYChart(
    states: List<List<State>>,
    title: String  = "",
    xLabel: String = "X-axis",
    yLabel: String = "Y-axis",
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
    lineChart.lookup(".chart-plot-background").style = "-fx-background-color: #ffffff;"
    lineChart.lookup(".chart-horizontal-grid-lines").style = "-fx-stroke: #ffffff;"
    lineChart.lookup(".chart-vertical-grid-lines").style = "-fx-stroke: #ffffff;"

    if (sort == "NONE")
        lineChart.axisSortingPolicy = LineChart.SortingPolicy.NONE

    for (state in states) {
        // define series
        val series = XYChart.Series<Number, Number>()
        //series.name = "Series"

        // populate series with data
        for (item in state) {
            val (x, y) = item
            series.data.add(XYChart.Data(x, y))
        }

        // assign series with data to LineChart
        lineChart.data.add(series)
        
        // comment out the next two lines for default color output
        val line: Node = series.node.lookup(".chart-series-line")
        line.style = "-fx-stroke-width: .25; -fx-stroke: #555555;"

    }
    // return LineChart object
    return lineChart
}