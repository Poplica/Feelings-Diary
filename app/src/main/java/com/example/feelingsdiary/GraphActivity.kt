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
        allAverages.clear()
        var formatter = SimpleDateFormat("MM-dd-yy")
        var calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        var today = LocalDate.now()
        var startDate = today
        var tempConvert = Date.from(today.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant())
        //var convertDate = formatter.parse(today.toString())
        Log.i("DateTesting", today.toString())
        var mmddyy = formatter.format(tempConvert)
        Log.i("DateTesting", mmddyy)
        //Log.i("DateTesting", convertDate.toString())
        var sum = 0.0
        var total = 0.0


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
        /*val gooseArr: ArrayList<String> = arrayListOf("5436534634")
        val journalEntry = JournalEntry(
            "gooseEgg",
            "gooseEgg",
            "gooseEgg",
            gooseArr,
            "gooseEgg"
        )*/
        Log.i("GraphActivity", startDate.toString())
        currUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                while(startDate.isBefore(today) || startDate.isEqual(today)) {
                    Log.i("GraphActivity", "Whileloop Day")
                    Log.i("GraphActivity", startDate.toString())
                    tempConvert = Date.from(startDate.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                    mmddyy = formatter.format(tempConvert)
                    Log.i("GraphActivity", mmddyy)
                    userRatingList!!.clear()
                    Log.i("Tag", dataSnapshot.childrenCount.toString())
                    for (simpleDates in dataSnapshot.children) {
                        Log.i("GraphActivity", "simpleDates.key then mmddyy below")
                        Log.i("GraphActivity", simpleDates.key)
                        Log.i("GraphActivity", mmddyy)
                        if(simpleDates.key == mmddyy) {
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
                        if(userRatingList!!.size != 0) {
                            total = sum / (userRatingList!!.size)
                            Log.i("The average is", total.toString())
                            allAverages.add(total)
                        }
                        else{
                            total = 0.0
                            allAverages.add(total)
                        }
                        Log.i("Average added for entries from", startDate.toString())

                    }
                    startDate = startDate.plusDays(1)
                    Log.i("The new date is", startDate.toString())
                    if(!startDate.isBefore(today) && !startDate.isEqual(today)){
                        break
                    }
                }
                drawGraph()
            }
            override fun onCancelled(databaseError: DatabaseError) {Log.i("GraphActivity", "onCancelled")}
        })
        Log.i("After free", "Free from the loop!")
        /*while (startDate.isBefore(today) || startDate.isEqual(today)) {
            Log.i("GraphActivity", "Whileloop Day")
            Log.i("GraphActivity", startDate.toString())
            tempConvert = Date.from(startDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant())
            mmddyy = formatter.format(tempConvert)
            Log.i("GraphActivity", mmddyy)
            currUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userRatingList!!.clear()
//                    Log.i("Tag", dataSnapshot.childrenCount.toString())
                    for (simpleDates in dataSnapshot.children) {
                        Log.i("GraphActivity", "simpleDates.key then mmddyy below")
                        Log.i("GraphActivity", simpleDates.key)
                        Log.i("GraphActivity", mmddyy)
                        if(simpleDates.key == mmddyy) {
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
            Log.i("The day is now", startDate.toString())
        }*/
        Log.i("After free", "Nothing weird happening")

        //val tempAxis = arrayOfNulls<Double>(allAverages!!.size)
        //val yAxisData = allAverages!!.toArray(tempAxis)
        if(userRatingList == null){
            Log.i("GraphActivity", "That jont null")
        }
        Log.i("allAverages", allAverages.size.toString())
        for(j in 0 .. allAverages!!.size - 1){
            Log.i("allAverages", allAverages.get(j).toString())
        }
        val yAxisData = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val yAxisValues = ArrayList<PointValue>()
        val axisValues = ArrayList<AxisValue>()
        val line = Line(yAxisValues)
        for (i in 0 until axisData.size) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in 0 until allAverages.size) {
            yAxisValues.add(PointValue(i.toFloat(), allAverages!!.get(i).toFloat()))
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

    private fun drawGraph() {
        super.onStart()
        Log.i("ENTERINGONSTART", "ONSTART")
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
        val yAxisValues = ArrayList<PointValue>()
        val axisValues = ArrayList<AxisValue>()
        val line = Line(yAxisValues)
        for (i in 0 until axisData.size) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in 0 until allAverages.size) {
            yAxisValues.add(PointValue(i.toFloat(), allAverages!!.get(i).toFloat()))
        }
        /*for (i in 0 until yAxisData.size) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i]!!.toFloat()))
        }*/
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
