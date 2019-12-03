package com.example.feelingsdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


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
import java.time.ZoneId


class GraphActivity : AppCompatActivity() {
    private val fire = FirebaseDatabase.getInstance().reference
    private val currUser = fire.child(FirebaseAuth.getInstance().currentUser!!.uid)
    private var userRatingList: ArrayList<Double>? = arrayListOf()
    private val allAverages = ArrayList<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val lineChartView = findViewById<LineChartView>(R.id.chart)
        val allDays = arrayOf(
            "SUN",
            "MON",
            "TUE",
            "WED",
            "THUR",
            "FRI",
            "SAT"
        )

        var formatter = SimpleDateFormat("MM-dd-yy")
        var calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        var today = LocalDate.now()
        var startDate = today
        var tempConvert = Date.from(today.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant())
        Log.i("DateTesting", today.toString())
        var mmddyy = formatter.format(tempConvert)
        Log.i("DateTesting", mmddyy)
        var sum = 0.0
        var total = 0.0
        var dayFound = 0


        when (day) {
            Calendar.SUNDAY -> {
                //calculate current day - 0
                Log.i("GraphActivity", "Sunday")
            }
            Calendar.MONDAY -> {
                startDate = today.minusDays(1)
                Log.i("GraphActivity", "Monday")
            }
            Calendar.TUESDAY -> {
                //calculate current day - 2
                startDate = today.minusDays(2)
                Log.i("GraphActivity", "Tuesday")
            }
            Calendar.WEDNESDAY->{
                //calculate current day - 3
                startDate = today.minusDays(3)
                Log.i("GraphActivity", "Wednesday")
            }
            Calendar.THURSDAY->{
                //calculate current day - 4
                startDate = today.minusDays(4)
                Log.i("GraphActivity", "Thursday")
            }
            Calendar.FRIDAY->{
                //calculate current day - 5
                startDate = today.minusDays(5)
                Log.i("GraphActivity", "Friday")
            }
            Calendar.SATURDAY->{
                //calculate current day - 6
                startDate = today.minusDays(6)
                Log.i("GraphActivity", "Saturday")
            }
            else->{
                //impossible
            }
        }

        Log.i("GraphActivity", startDate.toString())
        currUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allAverages.clear()
                //iterate through days from sunday onward
                while(startDate.isBefore(today) || startDate.isEqual(today)) {
                    Log.i("GraphActivity", "Whileloop Day")
                    Log.i("GraphActivity", startDate.toString())
                    tempConvert = Date.from(startDate.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                    mmddyy = formatter.format(tempConvert)
                    Log.i("GraphActivity", mmddyy)
                    Log.i("Tag", dataSnapshot.childrenCount.toString())
                    dayFound = 0
                    //iterate through days with entries
                    for (simpleDates in dataSnapshot.children) {
                        userRatingList!!.clear()
                        Log.i("GraphActivity", "simpleDates.key then mmddyy below")
                        Log.i("GraphActivity", simpleDates.key)
                        Log.i("GraphActivity", mmddyy)
                        //check to see if current day matches an entry day
                        if(simpleDates.key == mmddyy) {
                            //add up ratings and calculate average for that days entries
                            Log.i("GraphActivity", "check Works")
                            sum = 0.0
                            total = 0.0
                            for (entry in simpleDates.children) {
                                if(entry.getValue(JournalEntry::class.java)!!.getRating().toDouble() == null){
                                    Log.i("GraphActivity", "Rating or item null")
                                }
                                else {
                                    userRatingList!!.add(entry.getValue(JournalEntry::class.java)!!.getRating().toDouble())
                                    Log.i("Rating Added", entry.getValue(JournalEntry::class.java)!!.getRating())
                                }
                            }
                            for (i in userRatingList!!){
                                sum = sum + i
                                Log.i("Adding Ratings",  sum.toString())
                            }
                            //ADD CASE FOR NO ENTRIES
                        }
                        //add entries into the allAverages list
                        if(userRatingList!!.size != 0) {
                            total = sum / (userRatingList!!.size)
                            Log.i("The average is", total.toString())
                            if(dayFound == 0) {
                                allAverages.add(total)
                                dayFound = 1
                            }

                        }
                    }
                    //if day is not found in entries add a 0.0 value for that day
                    if(dayFound == 0){
                        allAverages.add(0.0)
                    }
                    //iterate to the next day, break if at end
                    startDate = startDate.plusDays(1)
                    Log.i("The new date is", startDate.toString())
                    if(!startDate.isBefore(today) && !startDate.isEqual(today)){
                        break
                    }
                }
                //funciton to draw real graph
                drawGraph()
            }
            override fun onCancelled(databaseError: DatabaseError) {Log.i("GraphActivity", "onCancelled")}
        })
        Log.i("After free", "Free from the loop!")

        val zeroBase = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val yVals = ArrayList<PointValue>()
        val xVals = ArrayList<AxisValue>()
        val line = Line(yVals)
        for (i in 0 until allDays.size) {
            xVals.add(i, AxisValue(i.toFloat()).setLabel(allDays[i]))
        }

        /*for (i in 0 until allAverages.size) {
            yAxisValues.add(PointValue(i.toFloat(), allAverages!!.get(i).toFloat()))
        }*/
        for (i in 0 until zeroBase.size) {
            yVals.add(PointValue(i.toFloat(), zeroBase[i]!!.toFloat()))
        }
        val lines = ArrayList<Line>()
        lines.add(line)
        val data = LineChartData()
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        val xAxis = Axis()
        xAxis.setValues(xVals)
        data.axisXBottom = xAxis
        val yAxis = Axis()
        data.axisYLeft = yAxis

    }

    private fun drawGraph() {
        Log.i("allAverages", allAverages.size.toString())
        for(j in 0 .. allAverages!!.size - 1){
            Log.i("allAverages", allAverages.get(j).toString())
        }
        val lineChartView = findViewById<LineChartView>(R.id.chart)
        val allDays = arrayOf(
            "SUN",
            "MON",
            "TUE",
            "WED",
            "THUR",
            "FRI",
            "SAT"
        )
        //below are demo values (IF ALLOWED) just incase we want to present a more populated graph (depending on what day of the week our presentation is)
        //val demoValues = doubleArrayOf(2.0, 3.5, 1.0, 4.3, 5.0, 1.5, 3.6)
        val yVals = ArrayList<PointValue>()
        val xVals = ArrayList<AxisValue>()
        val line = Line(yVals)
        for (i in 0 until allDays.size) {
            xVals.add(i, AxisValue(i.toFloat()).setLabel(allDays[i]))
        }

        for (i in 0 until allAverages.size) {
            yVals.add(PointValue(i.toFloat(), allAverages!!.get(i).toFloat()))
        }
        //loop adding demo values
        /*for (i in 0 until demoValues.size) {
            yVals.add(PointValue(i.toFloat(), demoValues[i].toFloat()))
        }*/

        val lines = ArrayList<Line>()
        lines.add(line)
        val data = LineChartData()
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        val xAxis = Axis()
        xAxis.setValues(xVals)
        data.axisXBottom = xAxis
        val yAxis = Axis()
        data.axisYLeft = yAxis

    }
}
