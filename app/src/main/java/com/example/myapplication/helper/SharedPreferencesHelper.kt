package com.example.myapplication.helper

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.activites.App
import com.example.myapplication.enums.SharedPreferencesKey

object SharedPreferencesHelper {
    private const val PREFERENCES_NAME = "MyPreferences"
    private const val ADMIN_INFO = "AdminInfo"
    private val sharedPreferences: SharedPreferences by lazy {
        App.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveAdminData(context: Context, email: String, password: String, role: String) {
        val sharedPreferences = context.getSharedPreferences(ADMIN_INFO, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SharedPreferencesKey.EMAIL.value, email)
        editor.putString(SharedPreferencesKey.PASSWORD.value, password)
        editor.putString(SharedPreferencesKey.ROLE.value, role)
        editor.apply()
    }

    fun getAdminEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(ADMIN_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SharedPreferencesKey.EMAIL.value, "") ?: ""
    }

    fun getAdminPassword(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(ADMIN_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SharedPreferencesKey.PASSWORD.value, "") ?: ""
    }

    fun getAdminRole(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(ADMIN_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SharedPreferencesKey.ROLE.value, "") ?: ""
    }

    fun saveUniversityName(universityName: String) {
        sharedPreferences.edit().putString("UNIVERSITY_NAME", universityName).apply()
    }

    fun getUniversityName(): String {
        return sharedPreferences.getString("UNIVERSITY_NAME", "") ?: ""
    }

    fun saveFacultyName(facultyName: String) {
        sharedPreferences.edit().putString("FACULTY_NAME", facultyName).apply();
    }

    fun getFacultyName (): String {
        return sharedPreferences.getString("FACULTY_NAME", "") ?: ""
    }
}