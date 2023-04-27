package com.salemflores.luna


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import database.Symptom
import androidx.room.Room
import database.SymptomDao
import database.SymptomDatabase
import kotlinx.coroutines.withContext


class SymptomActivity : AppCompatActivity() {

    private lateinit var symptomDao: SymptomDao
    private lateinit var selectedDate: String
    private lateinit var stomachCrampsCheckbox: CheckBox
    private lateinit var backCrampsCheckbox: CheckBox
    private lateinit var headacheCheckbox: CheckBox
    private lateinit var fatigueCheckbox: CheckBox
    private lateinit var acneCheckbox: CheckBox
    private lateinit var bloatingCheckbox: CheckBox
    private lateinit var nauseaCheckbox: CheckBox
    private lateinit var saveButton: Button

    private val symptomsList = mutableListOf<Symptom>()

    private lateinit var symptomsAdapter: SymptomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_symptom)

        selectedDate = intent.getStringExtra("selectedDate")!!

        stomachCrampsCheckbox = findViewById(R.id.stomachcramps)
        backCrampsCheckbox = findViewById(R.id.backcramps)
        headacheCheckbox = findViewById(R.id.headache)
        fatigueCheckbox = findViewById(R.id.fatigue)
        acneCheckbox = findViewById(R.id.acne)
        bloatingCheckbox = findViewById(R.id.bloating)
        nauseaCheckbox = findViewById(R.id.nausea)
        saveButton = findViewById(R.id.SaveButton)



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        symptomsAdapter = SymptomsAdapter(symptomsList)
        recyclerView.adapter = symptomsAdapter

        val db = Room.databaseBuilder(applicationContext, SymptomDatabase::class.java, "symptom_database").build()
        symptomDao = db.symptomDao()


        saveButton.setOnClickListener {
            val newSymptoms = mutableListOf<String>()
            if (stomachCrampsCheckbox.isChecked) {
                newSymptoms.add("Stomach Cramps")
            }
            if (backCrampsCheckbox.isChecked) {
                newSymptoms.add("Back Cramps")
            }
            if (headacheCheckbox.isChecked) {
                newSymptoms.add("Headache")
            }
            if (fatigueCheckbox.isChecked) {
                newSymptoms.add("Fatigue")
            }
            if (acneCheckbox.isChecked) {
                newSymptoms.add("Acne")
            }
            if (bloatingCheckbox.isChecked) {
                newSymptoms.add("Bloating")
            }
            if (nauseaCheckbox.isChecked) {
                newSymptoms.add("Nausea")
            }

            saveSymptoms(selectedDate, newSymptoms)

            fetchExistingSymptoms(selectedDate)

            val newSymptomsAsSymptoms = newSymptoms.map {
                Symptom(id = 0, date = selectedDate, symptom = it)
            }
            // add new symptoms to the existing list
            symptomsList.addAll(newSymptomsAsSymptoms)
            symptomsAdapter.notifyDataSetChanged()
            symptomsAdapter.setSymptoms(symptomsList)
        }

        val exitButton = findViewById<Button>(R.id.exit_button)
        exitButton.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            val savedSymptoms = savedInstanceState.getParcelableArrayList<Symptom>("symptoms")
            if (savedSymptoms != null) {
                symptomsList.addAll(savedSymptoms)
                updateRecyclerView()
            }
        } else {
            fetchExistingSymptoms(selectedDate)
        }

    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("symptoms", ArrayList(symptomsList))
    }

    private fun fetchExistingSymptoms(date: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val existingSymptoms = symptomDao.getSymptomsByDate(date)
            withContext(Dispatchers.Main) {
                existingSymptoms.forEach {
                    if (!symptomsList.contains(it)) {
                        symptomsList.add(it)
                    }
                }
                    symptomsAdapter.notifyDataSetChanged()
                    symptomsAdapter.setSymptoms(symptomsList)

            }
        }
    }

    private fun saveSymptoms(date: String,symptoms: List<String>) {
        lifecycleScope.launch(Dispatchers.IO) {
            symptoms.forEach {
                val symptom = Symptom(id = 0, date = selectedDate, symptom = it)
                symptomDao.insertSymptom(symptom)
            }
        }
    }

    private fun updateRecyclerView() {
        symptomsAdapter.notifyDataSetChanged()
    }
}








