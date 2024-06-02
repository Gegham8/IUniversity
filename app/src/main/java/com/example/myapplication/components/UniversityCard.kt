package com.example.myapplication.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.enums.InfoKeys
import com.example.myapplication.models.Faculty
import com.example.myapplication.models.University
import com.example.myapplication.activites.InformationPage
import com.google.gson.Gson

class UniversityCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout (context, attrs, defStyleAttr){
    private var imageResId: Int = 0
    private val imageView: ImageView
    private val textViewContainer: ConstraintLayout;
    private val accardionButton : Button;
    private val universityName: TextView
    private val foundedIn: TextView
    private val location: TextView
    private val phone: TextView
    private val rector: TextView
    private val card : ConstraintLayout;
    private var faculties: List<Faculty> = emptyList()
    init {
        LayoutInflater.from(context).inflate(R.layout.university_card, this, true)
        card = findViewById(R.id.card);
        imageView = findViewById(R.id.imageView)
        textViewContainer = findViewById(R.id.textContainer)
        accardionButton = findViewById(R.id.informationArrowBtn)
        universityName = findViewById(R.id.textView3);
        foundedIn = findViewById(R.id.founded_in)
        location = findViewById(R.id.location)
        phone = findViewById(R.id.phone)
        rector = findViewById(R.id.rector)
        card.setOnClickListener{
            val intent = Intent(context, InformationPage::class.java)
            intent.putExtra(InfoKeys.UNIVERSITY_NAME.toString(), universityName.text.toString())
            intent.putExtra(InfoKeys.IMAGE.toString(), imageResId)
            intent.putExtra(InfoKeys.FOUNDED_IN.toString(), foundedIn.text.toString())
            intent.putExtra(InfoKeys.LOCATION.toString(), location.text.toString())
            intent.putExtra(InfoKeys.PHONE.toString(), phone.text.toString())
            intent.putExtra(InfoKeys.RECTOR.toString(), rector.text.toString())
            val gson = Gson()
            val facultiesJson = gson.toJson(faculties)
            intent.putExtra(InfoKeys.FACULTIES.toString(), facultiesJson)
            context.startActivity(intent)
        }

        accardionButton.setOnClickListener {
            if (textViewContainer.visibility == VISIBLE) {
                textViewContainer.visibility = GONE
                accardionButton.setBackgroundResource(R.drawable.accardion);
            } else {
                textViewContainer.visibility = VISIBLE
                accardionButton.setBackgroundResource(R.drawable.accardion_back);
            }
        }
    }
    fun setValues(imageResId: Int, university: University) {
        this.imageResId = imageResId
        imageView.setImageResource(imageResId)
        universityName.text = university.name
        foundedIn.text = university.founded
        location.text = university.location
        phone.text = university.phoneNumber
        rector.text = university.rector
        faculties = university.faculties;
    }
}