package com.example.myapplication.activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.components.InformationCard
import com.example.myapplication.enums.InfoKeys
import com.example.myapplication.models.Faculty
import com.example.myapplication.models.University
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InformationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.information_view)
        setUpUI();
    }

    private fun setUpUI () {
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(R.drawable.baseline_arrow_back_24, null, null, MainActivity::class.java, null, null);
        loadInformation()
    }

    private fun loadInformation () {
        val parentLayout = findViewById<ConstraintLayout>(R.id.info);
        println(parentLayout.toString())
        val informationCard = InformationCard(this)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        informationCard.layoutParams = layoutParams;
        informationCard.setValues(intent.getUniversityExtras());
        parentLayout.addView(informationCard);
    }


    private fun Intent.getUniversityExtras(): University {
        val name = getStringExtra(InfoKeys.UNIVERSITY_NAME.toString())
        val imageResId = getIntExtra(InfoKeys.IMAGE.toString(), 0)
        val foundedIn = getStringExtra(InfoKeys.FOUNDED_IN.toString())
        val location = getStringExtra(InfoKeys.LOCATION.toString()).toString()
        val phone = getStringExtra(InfoKeys.PHONE.toString()).toString()
        val rector = getStringExtra(InfoKeys.RECTOR.toString()).toString()
        val facultiesJson = intent.getStringExtra(InfoKeys.FACULTIES.toString())
        val facultiesType = object : TypeToken<List<Faculty>>() {}.type
        val facultiesList: List<Faculty> = Gson().fromJson(facultiesJson, facultiesType)
        return University(
            name, imageResId, foundedIn,
            location, phone, rector, facultiesList
        )
    }

}