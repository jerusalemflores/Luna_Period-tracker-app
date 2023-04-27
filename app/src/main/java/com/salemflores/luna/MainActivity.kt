package com.salemflores.luna


import database.Symptom
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.CalendarView
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit


class MainActivity : AppCompatActivity() {
    private var symptomsList: List<Symptom> = emptyList()

    private lateinit var nextPeriodTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_main)



        val calendarView = findViewById<CalendarView>(R.id.CalendarView)
        val logButton = findViewById<Button>(R.id.LogButton)
        val foodButton = findViewById<Button>(R.id.FoodButton)
        val reminderButton = findViewById<Button>(R.id.ReminderButton)
        val estimateButton = findViewById<Button>(R.id.EstimateButton)
        nextPeriodTextView = findViewById(R.id.nextPeriodTextView)


        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            logButton.setOnClickListener {
                //val selectedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(calendarView.date))
                //showLogDialog(selectedDate)
                val selectedDate = "${month + 1}/$dayOfMonth/$year"
                if (symptomsList.isNotEmpty()) {
                    showSymptomActivity(selectedDate)
                } else {
                    showSymptomActivity(selectedDate)
                }
            }
        }

            estimateButton.setOnClickListener {
                //val selectedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(calendarView.date))
                //showLogDialog(selectedDate)

                showEstimateDialog()

            }


        //maybe this button can be for estimates....like dialog box pops up and says estimates and gives moon info

        //food button
        foodButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
        }
        //reminder button
        reminderButton.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

    }

 private fun showEstimateDialog() {
     val dialog = AlertDialog.Builder(this)
         .setTitle("Enter Previous Period Dates")
         .setView(R.layout.dialog_estimate)
         .setPositiveButton("OK", null)
         .setNegativeButton("Cancel", null)
         .create()

     dialog.setOnShowListener {
         val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
         val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
         val startDateString = dialog.findViewById<EditText>(R.id.startDateEditText)
         val endDateString = dialog.findViewById<EditText>(R.id.endDateEditText)
         val prevCycle1StartDateString = dialog.findViewById<EditText>(R.id.startDateEditText2)
         val prevCycle1EndDateString = dialog.findViewById<EditText>(R.id.endDateEditText2)


         okButton.setOnClickListener {

            try {
             // Get the start and end dates from the edit text
             val startDateString = startDateString?.text.toString()
             val endDateString = endDateString?.text.toString()
             val prevCycle1StartDateString = prevCycle1StartDateString?.text.toString()
             val prevCycle1EndDateString = prevCycle1EndDateString?.text.toString()


             val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
             val prevPeriodStart = LocalDate.parse(startDateString, formatter)
             val prevPeriodEnd = LocalDate.parse(endDateString, formatter)
             val prevCycle1Start = LocalDate.parse(prevCycle1StartDateString, formatter)
             val prevCycle1End = LocalDate.parse(prevCycle1EndDateString, formatter)


             // Calculate the length of the two previous menstrual cycles
             val cycleLength1 = prevPeriodEnd.dayOfMonth - prevPeriodStart.dayOfMonth + 1
             val cycleLength2 = prevCycle1End.dayOfMonth - prevCycle1Start.dayOfMonth + 1
             val averageCycleLength = (cycleLength1 + cycleLength2) / 2

                val daysBetweenCycles = prevPeriodEnd.until(prevCycle1End, ChronoUnit.DAYS)
                val nextPeriodStart = prevCycle1End.plusDays(averageCycleLength - daysBetweenCycles + 19)

                val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                val nextPeriodStartDateString = nextPeriodStart.format(dateFormatter)

                nextPeriodTextView.setText("Next period start date: " + nextPeriodStartDateString)

                // Display the predicted start date to the user
             Log.d("Next period start", nextPeriodStartDateString)
             } catch (e: Exception) {
                 Log.e("Error parsing date", e.message ?: "")
             }

             dialog.dismiss()
         }

         cancelButton.setOnClickListener {

             dialog.dismiss()
         }
     }
     dialog.show()
     }


    private fun showSymptomActivity(selectedDate: String) {
        val intent = Intent(this, SymptomActivity::class.java)
        intent.putExtra("selectedDate", selectedDate)
        startActivity(intent)
    }


}



