package com.example.myapplication.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.R
import com.example.myapplication.helper.DataManagerHelper
import com.example.myapplication.helper.SharedPreferencesHelper

class AdminUniversityNamesPage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.admin_university_names);
        setUpUI()
    }

    private fun setUpUI () {
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(R.drawable.baseline_arrow_back_24, null,null, LoginPage::class.java, null, null);
        loadUniversityData();
    }

    private fun loadUniversityData () {
        val universitiesData = DataManagerHelper.getUniversitiesData();
        val parentLayout = findViewById<ConstraintLayout>(R.id.university_names_wrapper)
        universitiesData?.let { universities ->
            var prevId = ConstraintLayout.LayoutParams.PARENT_ID
            for (university in universities) {
                val professionTextView = TextView(this).apply {
                    id = View.generateViewId()
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 10
                        leftMargin = 10
                        rightMargin = 10
                        bottomMargin = 10
                        if (prevId != ConstraintLayout.LayoutParams.PARENT_ID) {
                            topToBottom = prevId
                        }
                    }
                    setOnClickListener {
                        val universityName = university.name;
                        val intent = Intent(this@AdminUniversityNamesPage, AdminFacultyPage::class.java)
                        SharedPreferencesHelper.saveUniversityName(universityName);
                        startActivity(intent)
                    }
                    text = "${university.name}"
                    maxLines = 2
                    setPadding(0, 20, 0, 10)
                    background = ContextCompat.getDrawable(context, R.drawable.rounded_background)
                }
                parentLayout.addView(professionTextView)
                prevId = professionTextView.id
            }
        }
    }


}