/* KOTLIN FROM SCRATCH - Faisal Islam */
/* PROJECT 4: Visualize Data as a Bar Chart */

import javafx.application.Application
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.BarChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

class BarChartExample : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Bar Chart Example"

        // create XYAxis objects and set their properties
        val xAxis = CategoryAxis()
        val yAxis = NumberAxis()
        xAxis.label = "Months"
        yAxis.label = "Sales in thousands of dollars"

        // create BarChart object and set its properties
        val barChart = BarChart(xAxis, yAxis)
        barChart.title = "Monthly Sales"
        barChart.legendSide = Side.TOP

        // create a series, populate with data, assign to chart
        val dataSeries = XYChart.Series<String, Number>()
        dataSeries.name = "Q1 Data for ABC & Co."
        getData(dataSeries)
        barChart.data.add(dataSeries)

        val scene = Scene(barChart, 400.0, 400.0)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(BarChartExample::class.java)
}

// populate the series with data

fun getData(dataSeries: XYChart.Series<String, Number>) {
    dataSeries.data.addAll(
        XYChart.Data("Jan", 150),
        XYChart.Data("Feb", 100),
        XYChart.Data("Mar", 225)
    )
}
