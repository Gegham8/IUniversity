package com.example.myapplication.activites

import DialogUtils
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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

class AdminFacultyPage : AppCompatActivity() {

    private lateinit var universityName: String
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.admin_faculty_page);
        universityName = SharedPreferencesHelper.getUniversityName();
        setupUI()
    };

    override fun onRestart() {
        super.onRestart()
        setupUI()
    }

    private fun setupUI() {
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(R.drawable.baseline_arrow_back_24, null, null, AdminUniversityNamesPage::class.java, null, null);

        val universityNameTextView = findViewById<TextView>(R.id.university_name_data)
        universityNameTextView.text = universityName

        val addFacultyButton = findViewById<Button>(R.id.add)
        addFacultyButton.setOnClickListener {
            showAddFacultyDialog(universityName)
        }
        val deleteButton = findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener {
            showDeleteFacultyDialog(universityName)
        }

        loadFacultyData()
    }


    private fun loadFacultyData() {
        val universitiesData = DataManagerHelper.getUniversityByName(universityName)
        val parentLayout = findViewById<ConstraintLayout>(R.id.faculties_names_wrapper)

        universitiesData?.let { university ->
            var prevId = ConstraintLayout.LayoutParams.PARENT_ID
            for (faculty in university.faculties.withIndex()) {
                val deleteIcon = ContextCompat.getDrawable(this,
                    R.drawable.edit_content_svgrepo_com
                )
                val facultyTextView = TextView(this).apply {
                    id = View.generateViewId()
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 10
                        leftMargin = 10
                        rightMargin = 10
                        bottomMargin = 20
                        if (prevId != ConstraintLayout.LayoutParams.PARENT_ID) {
                            topToBottom = prevId
                        }
                        setOnClickListener {
                            val intent = Intent(this@AdminFacultyPage, ProfessionsPage::class.java);
                            DataManagerHelper.setFaculty(universityName, faculty.value.name);
                            SharedPreferencesHelper.saveFacultyName(faculty.value.name);
                            startActivity(intent)
                        }
                        setCompoundDrawablesWithIntrinsicBounds(null, null, deleteIcon, null)
                    }
                    text = faculty.value.name
                    maxLines = 2
                    setPadding(10, 20, 10, 20)
                    background = ContextCompat.getDrawable(context, R.drawable.rounded_background)
                }
                parentLayout.addView(facultyTextView)
                prevId = facultyTextView.id
            }
        }
    }

    private fun showAddFacultyDialog(universityName : String) {
        val dbController = DBController(this)
        val dialogView = layoutInflater.inflate(R.layout.add_faculty_dialog, null)
        val editTextFacultyName = dialogView.findViewById<EditText>(R.id.editTextFacultyName)
        val professionContainer = dialogView.findViewById<LinearLayout>(R.id.professionContainer)
        val buttonAddProfession = dialogView.findViewById<Button>(R.id.buttonAddProfession)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        buttonAddProfession.setOnClickListener {
            addProfessionEditText(professionContainer)
            addPriceEditText(professionContainer);
        }

        buttonSave.setOnClickListener {
            val facultyName = editTextFacultyName.text.toString()
            val professions = mutableListOf<String>()
            val prices = mutableListOf<String>()
            var isEmptyPriceFound = false
            if (facultyName.isEmpty()) {
                DialogUtils.showErrorDialog(this, DialogMessage.ENTER_FACULTY_NAME)
                return@setOnClickListener
            }
            for (i in 0 until professionContainer.childCount) {
                val childView = professionContainer.getChildAt(i)
                if (childView is EditText) {
                    val text = childView.text.toString()
                    if (text.isNotBlank()) {
                        if (i % 2 == 0) { // Even indices represent profession EditTexts
                            professions.add(text)
                        } else { // Odd indices represent price EditTexts
                            prices.add("${text}AMD")
                        }
                    } else if (i % 2 != 0) {
                        isEmptyPriceFound = true
                        break
                    }
                }
            }
            if (isEmptyPriceFound) {
                DialogUtils.showErrorDialog(this, DialogMessage.ENTER_PRICE)
                return@setOnClickListener
            }
            val facultyId = dbController.insertFacultyWithProfessions(facultyName, professions, prices, universityName)
            if (facultyId != -1L) {
                DialogUtils.showDialog(this, Status.SUCCESS, DialogMessage.FACULTY_ADDED_SUCCESSFULLY)
            } else {
                DialogUtils.showErrorDialog(this, DialogMessage.ERROR_TO_ADD_FACULTY);
            }
            dialog.dismiss()
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showDeleteFacultyDialog(universityName : String) {
        val dbConnector = DBController(this)
        val dialogView = layoutInflater.inflate(R.layout.delete_faculty_dialog, null)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val facultyNameEditText = dialogView.findViewById<EditText>(R.id.deleteTextFacultyName);
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        buttonSave.setOnClickListener {
            val facultyName = facultyNameEditText.text.toString()
            if (facultyName.isEmpty()) {
                DialogUtils.showErrorDialog(this, DialogMessage.ENTER_FACULTY_NAME)
                return@setOnClickListener
            }
            val facultyId =  dbConnector.deleteFacultyWithName(universityName, facultyName);
            if (facultyId != -1L) {
               DialogUtils.showDialog(this, Status.SUCCESS, DialogMessage.DELETED_SUCCESSFULLY)
            } else if (facultyId == -1L) {
                DialogUtils.showDialog(this, Status.NOT_FOUND, DialogMessage.FACULTY_NOT_FOUND);
            } else if (facultyId == -2L) {
                DialogUtils.showErrorDialog(this, DialogMessage.UNIVERSITY_NOT_FOUND);
            }
            dialog.dismiss()
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun addProfessionEditText(container: LinearLayout) {
        val editText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = "Profession"
        }
        container.addView(editText)
    }

    private fun addPriceEditText(container: LinearLayout) {
        val editText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = "Price"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        container.addView(editText)
    }



}