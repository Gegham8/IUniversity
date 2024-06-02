package com.example.myapplication.activites

import DialogUtils
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.myapplication.R
import com.example.myapplication.components.HeaderComponent
import com.example.myapplication.enums.DialogMessage
import com.example.myapplication.enums.Status
import com.example.myapplication.helper.SharedPreferencesHelper

class LoginPage  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_activity)
        setUpUI();
    }

    private fun setUpUI () {
        val passwordEditText = findViewById<AppCompatEditText>(R.id.password_text)
        val headerComponent = findViewById<HeaderComponent>(R.id.headerComponent);
        headerComponent.setIcons(R.drawable.baseline_arrow_back_24, null, null, MainActivity::class.java, null, null);
        passwordEditText.setOnClickListener {
            togglePasswordVisibility(passwordEditText)
        }
        val emailText = findViewById<EditText>(R.id.email_text)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        loginBtn.setOnClickListener {
            attemptLogin(emailText, passwordEditText)
        }
    }

    private fun attemptLogin(emailText: EditText, passwordText: EditText) {
        val stringEmail = emailText.text.toString()
        val stringPassword = passwordText.text.toString()
        if (stringEmail.isEmpty()) {
            emailText.error = "Please enter email"
            return
        } else if (stringPassword.isEmpty()) {
            passwordText.error = "Please enter a password"
            return
        }

        val email = SharedPreferencesHelper.getAdminEmail(this);
        val password = SharedPreferencesHelper.getAdminPassword(this);
        if (email.equals(stringEmail, ignoreCase = true) && password.equals(stringPassword)) {
            startActivity(Intent(this, AdminUniversityNamesPage::class.java))
        } else {
            DialogUtils.showDialog(this, Status.INVALID, DialogMessage.INVALID_USER)
        }
    }
    private fun togglePasswordVisibility(passwordText: EditText) {
        if (passwordText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + InputType.TYPE_CLASS_TEXT) {
            passwordText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT
        } else {
            passwordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + InputType.TYPE_CLASS_TEXT
        }
        passwordText.setSelection(passwordText.text.length)
    }

}