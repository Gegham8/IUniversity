package com.example.myapplication.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.models.University

class InformationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){
    private val imageView: ImageView
    private val universityName: TextView
    private val foundedIn: TextView
    private val location: TextView
    private val phone: TextView
    private val rector: TextView
    private val facultyButton : Button;
    private val parentLayout : ConstraintLayout
    private val faculties_text_views_wrapper : ConstraintLayout;
    private val profession_view_wrapper : ConstraintLayout;
    private val professionViewWrapperMap: MutableMap<String, ConstraintLayout> = mutableMapOf()
    init {
        LayoutInflater.from(context).inflate(R.layout.information_card, this, true);
        imageView = findViewById(R.id.university_image)
        universityName = findViewById(R.id.university_name)
        foundedIn = findViewById(R.id.founded_view)
        location = findViewById(R.id.location_view)
        phone = findViewById(R.id.phone_view)
        rector = findViewById(R.id.rector_view)
        parentLayout = findViewById(R.id.text_views_wrapper)
        facultyButton = findViewById(R.id.facultyButton);
        faculties_text_views_wrapper = findViewById(R.id.faculties_text_views_wrapper)
        profession_view_wrapper = findViewById(R.id.profession_view_wrapper)

        facultyButton.setOnClickListener{
            if (parentLayout.visibility == View.VISIBLE) {
                parentLayout.visibility = View.GONE
                faculties_text_views_wrapper.visibility = View.VISIBLE  // Change to VISIBLE here
                val accardion_back = resources.getDrawable(R.drawable.accardion_back)
                facultyButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, accardion_back, null)
            } else {
                parentLayout.visibility = View.VISIBLE
                faculties_text_views_wrapper.visibility = View.GONE
                val accardion = resources.getDrawable(R.drawable.accardion)
                facultyButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, accardion, null)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun setValues(university: University) {
        imageView.setImageResource(university.imageResId)
        universityName.text = university.name
        foundedIn.text = university.founded
        location.text = university.location
        phone.text = university.phoneNumber
        rector.text = university.rector

        var previousViewId = R.id.phone_view
        val facultyProfessionsMap = mutableMapOf<Int, MutableList<Int>>()
        for ((index, faculty) in university.faculties.withIndex()) {
            val facultyTextView = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                text = faculty.name
                setPadding(20, 20, 20, 10)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.accardion, 0)
            }
            faculties_text_views_wrapper.addView(facultyTextView)
            facultyProfessionsMap[facultyTextView.id] = mutableListOf()
            val facultySet = ConstraintSet().apply {
                clone(faculties_text_views_wrapper)
                if (index == 0) {
                    connect(facultyTextView.id, ConstraintSet.TOP, previousViewId, ConstraintSet.BOTTOM, 20)
                } else {
                    connect(facultyTextView.id, ConstraintSet.TOP, previousViewId, ConstraintSet.BOTTOM, 20)
                }
                applyTo(faculties_text_views_wrapper)
            }

            previousViewId = facultyTextView.id

            for (profession in faculty.professions) {
                val professionTextView = TextView(context).apply {
                    id = View.generateViewId()
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 10
                    }
                    visibility = View.GONE
                    text = "${profession.name} : ${profession.price}"
                    maxLines = 2
//                    setPadding(0, 20, 0, 10)
                    background = ContextCompat.getDrawable(context, R.drawable.rounded_background)
                }
                faculties_text_views_wrapper.addView(professionTextView)

                facultyProfessionsMap[facultyTextView.id]?.add(professionTextView.id)

                ConstraintSet().apply {
                    clone(faculties_text_views_wrapper)
                    connect(professionTextView.id, ConstraintSet.TOP, previousViewId, ConstraintSet.BOTTOM, 10)
                    connect(professionTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(professionTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                    applyTo(faculties_text_views_wrapper)
                }

                previousViewId = professionTextView.id
            }

            facultyTextView.setOnClickListener {
                var isAnyProfessionVisible = false

                facultyProfessionsMap[facultyTextView.id]?.forEach { professionId ->
                    faculties_text_views_wrapper.findViewById<TextView>(professionId).let { professionTextView ->
                        professionTextView.visibility = if (professionTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                        if (professionTextView.visibility == View.VISIBLE) {
                            isAnyProfessionVisible = true
                        }
                    }
                }

                val drawableEnd = if (isAnyProfessionVisible) R.drawable.accardion_back else R.drawable.accardion
                facultyTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableEnd, 0)

                facultyTextView.invalidate()
            }
        }

    }
}