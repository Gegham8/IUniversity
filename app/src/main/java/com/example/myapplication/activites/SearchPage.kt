package com.example.myapplication.activites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.database.DBController
import com.google.android.material.textfield.TextInputEditText

class SearchPage : AppCompatActivity() {
    private lateinit var dbHelper: DBController;
    private lateinit var resultContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.search_layout)
        dbHelper = DBController(this);
        setUpUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpUI() {
        resultContainer = findViewById(R.id.resultContainer)
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(
            R.drawable.baseline_arrow_back_24,
            R.drawable.login_svgrepo_com,
            null,
            MainActivity::class.java,
            LoginPage::class.java,
            null
        );

        val textInputEditText = findViewById<TextInputEditText>(R.id.search_input)
        Log.d("SetupUI", "Setting up UI")
        Log.d("SetupUI", "Header component icons set")
        textInputEditText.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = textInputEditText.compoundDrawables[DRAWABLE_RIGHT]
                if (drawableRight != null) {
                    val drawableWidth = drawableRight.bounds.width()
                    val touchX = event.rawX.toInt()
                    val drawableRightStart = textInputEditText.right - textInputEditText.paddingRight - drawableWidth
                    if (touchX >= drawableRightStart) {
                        val searchText = textInputEditText.text.toString().trim()
                        val formattedSearchText = searchText.toLowerCase().capitalize();
                        val data = dbHelper.searchProfession(formattedSearchText)
                        resultContainer.removeAllViews() // Clear existing views
                        if (data.isEmpty()) {
                            val messageTextView = TextView(this)
                            messageTextView.text = "Տվյալներ չեն գտնվել"
                            messageTextView.setPadding(16, 16, 16, 16) // Add padding
                            resultContainer.addView(messageTextView)
                        } else {
                        for (result in data) {
                            val resultView = LayoutInflater.from(this).inflate(R.layout.result_item_layout, resultContainer, false) as CardView
                            resultView.findViewById<TextView>(R.id.resultTextView).text = "${result.first} - ${result.second}"
                            resultContainer.addView(resultView)
                            }
                        }

                        return@setOnTouchListener true


                        return@setOnTouchListener true
                    }
                }
            }
            return@setOnTouchListener false
        }
    }
    }