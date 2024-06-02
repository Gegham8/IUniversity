package com.example.myapplication.database;

import static com.example.myapplication.activites.App.context;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.myapplication.models.University;
import java.util.List;

public class DBController  {
    private final Context _context;
    private SQLiteDatabase _db;
    private DBHelper dbHelper = new DBHelper(context);
    public DBController(Context con) {
        this._context = con;
    }

    public void openDatabase() {
        DBHelper dbConnector = new DBHelper(_context);
        _db = dbConnector.getWritableDatabase();
    }

    public List<Pair<String, String>> searchProfession(String professionName ) {
        return  dbHelper.searchProfessionsInUniversities(professionName);
    }


    public long insertFacultyWithProfessions(String facultyName, List<String> professions,List<String> prices, String universityName) {
        return dbHelper.insertFacultyWithProfessions(facultyName, professions, prices, universityName);
    }

    public long deleteFacultyWithName(String universityName, String facultyName) {
        return dbHelper.deleteFacultyWithName(universityName, facultyName);
    }

    public int updateProfession(String universityName, String facultyName, String updatedProfessionName, String updatedprice,  String professionName, String price) {
        return dbHelper.updateProfession(universityName, facultyName, updatedProfessionName, updatedprice, professionName, price);
    }


    @SuppressLint("Range")
    public List<University> getAllUniversities() {
        return dbHelper.getAllUniversities();
    };

    public void closeDB() {
        if (_db != null && _db.isOpen()) {
            _db.close();
        }
    }
}


