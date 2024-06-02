package com.example.myapplication.activites

import DialogUtils
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.myapplication.database.DBController
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.R
import com.example.myapplication.enums.DialogMessage
import com.example.myapplication.enums.Status
import com.example.myapplication.helper.DataManagerHelper
import com.example.myapplication.helper.SharedPreferencesHelper

class ProfessionsPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var facultyName : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.professions_page);
        setUpUI();
    }

    private fun setUpUI () {
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(R.drawable.baseline_arrow_back_24, null, null, AdminFacultyPage::class.java, null, null);
        facultyName = findViewById(R.id.faculty_name);
        facultyName.text = SharedPreferencesHelper.getFacultyName();
        loadProfessionData();
    }

    private fun loadProfessionData () {
        val facultyData = DataManagerHelper.getFacultyByName(facultyName.text.toString())
        val parentLayout = findViewById<ConstraintLayout>(R.id.professions_name_wrapper)
        var prevId = ConstraintLayout.LayoutParams.PARENT_ID
        facultyData?.professions?.forEachIndexed { index, profession ->
            val professionButton = Button(this).apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 20
                    leftMargin = 10
                    rightMargin = 10
                    bottomMargin = 10
                    if (index != 0) {
                        topToBottom = prevId
                    }
                }
                setOnClickListener{
                    showAddProfessionDialog(profession.name, profession.price);
                }
                text = "${profession.name} ${profession.price}"
                maxLines = 2
                setPadding(0, 20, 0, 10)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_content_svgrepo_com, 0)
                background = ContextCompat.getDrawable(context, R.drawable.rounded_background)
            }
            parentLayout.addView(professionButton)
            prevId = professionButton.id
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddProfessionDialog(name: String, price: String) {
        val dbConnector = DBController(this);
        val dialogView = layoutInflater.inflate(R.layout.edit_proffesion_dialog, null)
        val editTextFacultyName = dialogView.findViewById<EditText>(R.id.edit_profession_name).apply {
            setText(name)
        }
        val editTextPrice = dialogView.findViewById<EditText>(R.id.edit_profession_price).apply {
            setText(price)
        }
        val buttonSave = dialogView.findViewById<Button>(R.id.save)
        val buttonCancel = dialogView.findViewById<Button>(R.id.cancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        buttonSave.setOnClickListener {
            val newProfessionName = editTextFacultyName.text.toString()
            val newPrice = editTextPrice.text.toString()
            val facultyName = SharedPreferencesHelper.getFacultyName();
            if (facultyName.isEmpty()) {
                DialogUtils.showErrorDialog(this, DialogMessage.FACULTY_NOT_FOUND);
                return@setOnClickListener
            }
            val universityName = DataManagerHelper.getUniversityName()
            if (newProfessionName.isEmpty() || newPrice.isEmpty()) {
                DialogUtils.showErrorDialog(this, DialogMessage.ENTER_NAME_AND_PRICE);
                return@setOnClickListener
            }
            val result = dbConnector.updateProfession(universityName, facultyName, newProfessionName, newPrice, name, price)
            val message = if (result.toLong() != -1L) DialogMessage.UPDATED_SUCCESSFULLY else DialogMessage.ERROR_IN_UPDATE
            DialogUtils.showDialog(this, Status.SUCCESS, message);
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}