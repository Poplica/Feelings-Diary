package com.example.feelingsdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import lecho.lib.hellocharts.view.LineChartView

import lecho.lib.hellocharts.model.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T







class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val lineChartView = findViewById<LineChartView>(R.id.chart)
        val axisData = arrayOf(
            "SUN",
            "MON",
            "TUE",
            "WED",
            "THUR",
            "FRI",
            "SAT"
        )

        val yAxisData = doubleArrayOf(0.5, 2.3, 1.1, 4.6, 2.7, 1.0, 4.1)
        val yAxisValues = ArrayList<PointValue>()
        val axisValues = ArrayList<AxisValue>()
        val line = Line(yAxisValues)
        for (i in 0 until axisData.size) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in 0 until yAxisData.size) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i].toFloat()))
        }
        val lines = ArrayList<Line>()
        lines.add(line)
        val data = LineChartData()
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        val axis = Axis()
        axis.setValues(axisValues)
        data.axisXBottom = axis
        val yAxis = Axis()
        data.axisYLeft = yAxis


    }
}
