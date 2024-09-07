/* KOTLIN FROM SCRATCH - Faisal Islam */
/* Projects 20: The Physics of Coffee Cooling */

// import block
import javafx.application.Application
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.chart.*
import javafx.stage.Stage
import java.text.DecimalFormat
import kotlin.math.exp

// data classes
data class State(
    val time: Double,
    val Temp: Double
)

// problem definition and global parameters
val coffeeT0 = 92.0      // degrees Celsius
val coffeeV = 250.0      // mL
val coffeeS = 4.190      // J/(gm C) - assumed same as water
val coffeeD = 1.0        // gm/mL - assumed same as water
val coffeeK = 0.0116     // 1/min

val milkT0 = 4.0         // degrees Celsius
val milkV = 25.0         // mL
val milkS = 3.890        // J/(gm C)
val milkD = 1.035        // gm/mL

val T_ambient = 20.0     // degrees Celsius
val timeMax = 25.0       // min (length of drive)
val timeStep = 0.25      // min

val df = DecimalFormat("#.##")

// ------------------------------------------------------------------------

// application class
class MixCoffeeAndMilk : Application() {
    override fun start(primaryStage: Stage) {
        val root = VBox()
        val scroll = ScrollPane()
        scroll.content = root
        val scene = Scene(scroll, 550.0, 600.0, Color.WHITE)
        primaryStage.title = "Coffee Cooling Profile"
        primaryStage.scene = scene
        primaryStage.show()

        // execute steps for coffee cooling process

        println("\n  *** Coffee Cooling Problem ***  \n")

        // step 1:
        val state1 =
            newtonCooling(T0=coffeeT0, Ta=T_ambient,
                k=coffeeK,tMax=timeMax, dt=timeStep)
        printTimeAndTemp(state1.last(), 1)

        // step 2:
        val finalT1 =
            tempAfterMixing(d1=coffeeD, v1=coffeeV, s1=coffeeS,
                T1=state1.last().Temp,
                d2=milkD, v2=milkV,
                s2=milkS, T2=milkT0)
        println("step 2: final temp with milk: " +
                "${df.format(finalT1)} degrees Celsius\n")

        // step 3:
        val initT2 =
            tempAfterMixing(d1=coffeeD, v1=coffeeV, s1=coffeeS,
                T1=coffeeT0, d2=milkD, v2=milkV,
                s2=milkS, T2=milkT0)
        println("step 3: initial temp with milk: " +
                "${df.format(initT2)} degrees Celsius")

        // step 4:
        val state2 =
            newtonCooling(T0=initT2, Ta=T_ambient, k=coffeeK,
                tMax=timeMax, dt=timeStep)
        printTimeAndTemp(state2.last(), 4)

        // step 5:
        val state3 =
            newtonCooling(T0=finalT1, Ta=T_ambient,
                k=coffeeK,tMax=timeMax, dt=timeStep,
                start=timeMax)

        val state4 =
            newtonCooling(T0=state2.last().Temp,
                Ta=T_ambient, k=coffeeK,
                tMax=timeMax, dt=timeStep, start=timeMax)

        val states =
            listOf(state1, state2, state3, state4)

        createCoolingChart(root, states=states)
    }
}

fun main() {
    Application.launch(MixCoffeeAndMilk::class.java)
}

// ------------------------------------------------------------------------

fun printTimeAndTemp(datapoint: State, step: Int) {
    val (endTime, endTemp ) = datapoint

    println("step $step: end time: ${df.format(endTime)} minutes")
    println("step $step: end temp: ${df.format(endTemp)} " +
            "degrees Celsius")
}

// ------------------------------------------------------------------------

fun newtonCooling(T0: Double, Ta: Double, k:Double,
                  tMax: Double, dt: Double,
                  start: Double = 0.0): List<State> {
    val state = mutableListOf<State>()
    var t = 0.0
    while (t <= tMax) {
        val temp = Ta + (T0 - Ta)*exp(-k * t)
        state += State(t+start, temp)
        t += dt
    }
    return state
}

// ------------------------------------------------------------------------

fun tempAfterMixing(
    d1: Double, v1: Double, s1: Double, T1: Double,
    d2: Double, v2: Double, s2: Double, T2: Double
): Double {

    return (d1 * v1 * s1 * T1 + d2 * v2 * s2 * T2) /
            (d1 * v1 * s1 + d2 * v2 * s2)
}

// ------------------------------------------------------------------------

fun createCoolingChart(root: VBox, states: List<List<State>>) {

    val xyChart =
        singleXYChart(states,
            title = "Temperature of Coffee Over Time",
            xLabel = "Time",
            yLabel = "Temperature (degrees Celsius)")

    root.children.add(xyChart)
}

// ------------------------------------------------------------------------

fun singleXYChart(
    states: List<List<State>>,
    title: String  = "",
    xLabel: String = "I am x-axis",
    yLabel: String = "I am y-axis",
    sort: String = "default"): LineChart<Number, Number> {

    // define axes
    val xAxis = NumberAxis()
    val yAxis = NumberAxis()
    xAxis.label = xLabel
    yAxis.label = yLabel
    yAxis.isAutoRanging = false
    xAxis.isAutoRanging = false
    yAxis.lowerBound = 40.0
    yAxis.upperBound = 100.0
    xAxis.lowerBound = 0.0
    xAxis.upperBound = 55.0

    // create LineChart
    val lineChart = LineChart(xAxis, yAxis)
    lineChart.title = title

    lineChart.createSymbols = false
    lineChart.isLegendVisible = false
    lineChart.lookup(".chart-plot-background").style = "-fx-background-color: #ffffff;"

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
        val line: Node = series.node.lookup(".chart-series-line")
        line.style = "-fx-stroke-width: 2; -fx-stroke: #000000;"
    }

    // return LineChart object
    return lineChart
}