package com.salemflores.luna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FoodActivity : AppCompatActivity() {

    private lateinit var foodRecommendationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food2)

        foodRecommendationTextView = findViewById(R.id.food_recommendation_text_view)

        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        val foodRecommendations = resources.getStringArray(R.array.food_recommendations)
        val randomIndex = Random(dayOfYear.toLong()).nextInt(foodRecommendations.size)

        val randomFoodRecommendation = foodRecommendations[randomIndex]

        foodRecommendationTextView.text = randomFoodRecommendation

        val thumbsUpButton = findViewById<Button>(R.id.thumbs_up_button)
        thumbsUpButton.setOnClickListener {
            Snackbar.make(it, "we're glad you liked it :)", Snackbar.LENGTH_SHORT).show()
        }

        val dislikeButton = findViewById<Button>(R.id.dislike_button)
        dislikeButton.setOnClickListener {
            Snackbar.make(it, "we'll try to do better next time :[", Snackbar.LENGTH_SHORT).show()
        }

        val exitButton = findViewById<Button>(R.id.exit_button)
        exitButton.setOnClickListener {
            finish()
        }


        }
}