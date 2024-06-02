package com.example.myapplication.activites

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.database.DBController
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.R
import com.example.myapplication.components.UniversityCard
import com.example.myapplication.helper.DataManagerHelper
import com.example.myapplication.helper.SharedPreferencesHelper
import com.example.myapplication.models.University

@SuppressLint("StaticFieldLeak")
object App {
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
        internal set
};
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        App.context = applicationContext
        setContentView(R.layout.main_page)
        SharedPreferencesHelper.saveAdminData(this,"ADMIN", "ADMIN", "ADMIN");
        setUpUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.right, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpUI () {
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent)
        headerComponent.setIcons(
            R.drawable.home_svgrepo_com,
            R.drawable.login_svgrepo_com,
            R.drawable.icons8_search,
            null,
            LoginPage::class.java,
            SearchPage::class.java)
        loadData()
    }

    private fun loadData () {
        val dbHelper = DBController(this);
        val parentLayout = findViewById<ConstraintLayout>(R.id.main)
        val imageCardView = UniversityCard(this)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        imageCardView.layoutParams = layoutParams;
        val imageResourceIds = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h
        )

        val universities: List<University> = dbHelper.allUniversities
        val universitiesData = ArrayList<University>()
        universities.forEachIndexed { index, university ->
            universitiesData.add(university);
            val universityCard = UniversityCard(this)
            universityCard.id = View.generateViewId()
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            if (index > 0) {
                layoutParams.topToBottom = parentLayout.getChildAt(index - 1 ).id
            }
            universityCard.layoutParams = layoutParams
            universityCard.setValues(
                imageResourceIds[index],
                university
            )
            parentLayout.addView(universityCard)
        }
        DataManagerHelper.setUniversitiesData(universitiesData);
    }


}