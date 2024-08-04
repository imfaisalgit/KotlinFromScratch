/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* PROJECT 5: Create a Multieeries Line Chart */

import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

class LineChartExample : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Multi-series Line Chart Example"

        // create XYAxis objects and set their properties
        val xAxis = NumberAxis()
        xAxis.label = "Age"
        val yAxis = NumberAxis()
        yAxis.label = "Height (inches)"

        // adjust tick interval and lower/upper bounds
        xAxis.isAutoRanging = false
        xAxis.tickUnit = 5.0    // custom tick interval
        xAxis.lowerBound = 0.0  // minimum value for x-axis
        xAxis.upperBound = 35.0 // maximum value for x-axis

        yAxis.isAutoRanging = false
        yAxis.lowerBound = 20.0 // minimum value for y-axis
        yAxis.upperBound = 75.0 // maximum value for y-axis

        // create LineChart object and set its properties
        val lineChart = LineChart(xAxis, yAxis)
        lineChart.title = "Average Heights at Different Ages"
        lineChart.legendSide = Side.TOP

        // the following two lines are not mentioned in the book
        // shows how to format chart title and legend
        lineChart.lookup(".chart-title").style = "-fx-font: 30.0 'Times Roman';"
        lineChart.lookup(".chart-legend").style = "-fx-font: 20.0 'Times Roman';"

        // crete series, populate with data, assign to chart
        val maleData = XYChart.Series<Number, Number>()
        maleData.name = "Male"
        getMaleData(maleData)

        val femaleData = XYChart.Series<Number, Number>()
        femaleData.name = "Female"
        getFemaleData(femaleData)

        lineChart.data.addAll(maleData, femaleData)

        val scene = Scene(lineChart, 800.0, 600.0)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(LineChartExample::class.java)
}

// provide male data points

fun getMaleData(maleData: XYChart.Series<Number, Number>) {
    maleData.data.addAll(
        XYChart.Data(5, 38.0),
        XYChart.Data(10, 50.0),
        XYChart.Data(15, 62.0),
        XYChart.Data(20, 68.0),
        XYChart.Data(30, 69.0)
    )
}

// provide female data points

fun getFemaleData(femaleData: XYChart.Series<Number, Number>) {
    femaleData.data.addAll(
        XYChart.Data(5, 36.0),
        XYChart.Data(10, 48.0),
        XYChart.Data(15, 60.0),
        XYChart.Data(20, 64.0),
        XYChart.Data(30, 65.0)
    )
}