package com.example.feelingsdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import lecho.lib.hellocharts.view.LineChartView

import lecho.lib.hellocharts.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.time.LocalDate


class GraphActivity : AppCompatActivity() {
    private val fire = FirebaseDatabase.getInstance().reference
    private val currUser = fire.child(FirebaseAuth.getInstance().currentUser!!.uid)
    private var userRatingList: ArrayList<Double>? = arrayListOf()
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

        //val yAxisData = doubleArrayOf()
        //val axisValues = ArrayList<AxisValue>()
        val allAverages = ArrayList<Double>()
        var formatter = SimpleDateFormat("MM-dd-yy")
        var calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        var today = LocalDate.now()
        var startDate = today

        var convertDate = formatter.parse(today.toString())
        var mmddyy = formatter.format(convertDate)
        var sum = 0.0
        var total = 0.0


        when (day) {
            Calendar.SUNDAY -> {
                //calculate current day - 0
            }
            Calendar.MONDAY -> {
                startDate = today.minusDays(1)
            }
            Calendar.TUESDAY -> {
                //calculate current day - 2
                startDate = today.minusDays(2)
            }
            Calendar.WEDNESDAY->{
                //calculate current day - 3
                startDate = today.minusDays(3)
            }
            Calendar.THURSDAY->{
                //calculate current day - 4
                startDate = today.minusDays(4)
            }
            Calendar.FRIDAY->{
                //calculate current day - 5
                startDate = today.minusDays(5)
            }
            Calendar.SATURDAY->{
                //calculate current day - 6
                startDate = today.minusDays(6)
            }
            else->{
                //impossible
            }
        }
        while (startDate.isBefore(today) || startDate.isEqual(today)) {
            convertDate = formatter.parse(startDate.toString())
            mmddyy = formatter.format(convertDate)
            currUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userRatingList!!.clear()
                    for (simpleDates in dataSnapshot.children) {
                        if(simpleDates.key == mmddyy) {
                            sum = 0.0
                            total = 0.0
                            for (entry in simpleDates.children) {
                                userRatingList!!.add(entry.getValue(JournalEntry::class.java)!!.getRating().toDouble())
                            }
                            for (i in userRatingList!!){
                                sum = sum + i
                            }
                            //ADD CASE FOR NO ENTRIES
                        }
                        if(userRatingList!!.size != 0) {
                            total = sum / (userRatingList!!.size)
                        }
                        else{
                            total = 0.0
                        }
                        allAverages.add(total)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            startDate = startDate.plusDays(1)
        }
        val tempAxis = arrayOfNulls<Double>(userRatingList!!.size)
        val yAxisData = userRatingList!!.toArray(tempAxis)
        val yAxisValues = ArrayList<PointValue>()
        val axisValues = ArrayList<AxisValue>()
        val line = Line(yAxisValues)
        for (i in 0 until axisData.size) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in 0 until yAxisData.size) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i]!!.toFloat()))
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
