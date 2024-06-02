package com.example.myapplication.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import android.util.Pair;

import com.example.myapplication.R;
import com.example.myapplication.enums.InfoKeys;
import com.example.myapplication.helper.UniversityDataReader;
import com.example.myapplication.models.Faculty;
import com.example.myapplication.models.Profession;
import com.example.myapplication.models.University;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Buh.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public SQLiteDatabase getWritableDatabase() {
        if (database == null) {
            database = super.getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        List<University> universities = UniversityDataReader.readUniversityDataFromJson(context, R.raw.university_data);
        if (universities != null) {
            for (University university : universities) {
                long universityId = insertUniversity(db, university);
                if (universityId != -1) {
                    for (Faculty faculty : university.getFaculties()) {
                        long facultyId = insertFaculty(db, faculty, universityId);
                        if (facultyId != -1) {
                            for (Profession profession : faculty.getProfessions()) {
                                insertProfession(db, profession, facultyId);
                            }
                        }
                    }
                }
            }
        }
    }


    private long insertUniversity(SQLiteDatabase db, University university) {
        ContentValues values = new ContentValues();
        values.put(InfoKeys.UNIVERSITY_NAME.getValue(), university.getName());
        values.put(InfoKeys.FOUNDED_IN.getValue(), university.getFounded());
        values.put(InfoKeys.RECTOR.getValue(), university.getRector());
        values.put(InfoKeys.LOCATION.getValue(), university.getLocation());
        values.put(InfoKeys.PHONE.getValue(), university.getPhoneNumber());
        return db.insert("University", null, values);
    }

    private long insertFaculty(SQLiteDatabase db, Faculty faculty, long universityId) {
        ContentValues values = new ContentValues();
        values.put(InfoKeys.FACULTY_NAME.getValue(), faculty.getName());
        values.put(InfoKeys.UNIVERSITY_ID.getValue(), universityId);
        return db.insert("Faculty", null, values);
    }

    private void insertProfession(SQLiteDatabase db, Profession profession, long facultyId) {
        ContentValues values = new ContentValues();
        values.put(InfoKeys.PROFESSION_NAME.getValue(), profession.getName());
        values.put(InfoKeys.PRICE.getValue(), profession.getPrice());
        values.put(InfoKeys.FACULTY_ID.getValue(), facultyId);
        db.insert("Profession", null, values);
    }

    @SuppressLint("Range")
    public long getUniversityIdByName(String universityName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long universityId = -1;

        Cursor cursor = db.query(
                "University", // Table name
                new String[]{"university_id"}, // Columns to retrieve
                "university_name=?", // WHERE clause
                new String[]{universityName}, // Values for the WHERE clause
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            universityId = cursor.getLong(cursor.getColumnIndex("university_id"));
        }

        cursor.close();
        return universityId;
    }

    public long insertFacultyWithProfessions(String facultyName, List<String> professions,List<String> prices, String universityName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long universityId = getUniversityIdByName(universityName);
        ContentValues facultyValues = new ContentValues();
        facultyValues.put(InfoKeys.FACULTY_NAME.getValue(), facultyName);
        facultyValues.put(InfoKeys.UNIVERSITY_ID.getValue(), universityId);
        long facultyId = db.insert("Faculty", null, facultyValues);
        if (facultyId != -1) {
            for (int i = 0; i < professions.size(); i++) {
                String profession = professions.get(i);
                String price = (i < prices.size()) ? prices.get(i) : "";
                ContentValues professionValues = new ContentValues();
                professionValues.put(InfoKeys.PROFESSION_NAME.getValue(), profession);
                professionValues.put(InfoKeys.PRICE.getValue(), price);
                professionValues.put(InfoKeys.FACULTY_ID.getValue(), facultyId);
                db.insert("Profession", null, professionValues);
            }
        }
        db.close();
        return facultyId;
    }

    @SuppressLint("Range")
    public long deleteFacultyWithName(String universityName, String facultyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long universityId = getUniversityIdByName(universityName);

        if (universityId != -1) {
            Cursor cursor = database.query(
                    "Faculty", // Table name
                    new String[]{"faculty_id"}, // Columns to retrieve
                    "University_id=? AND faculty_name=?", // WHERE clause
                    new String[]{String.valueOf(universityId), facultyName}, // Values for the WHERE clause
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            long facultyId = -1;
            if (cursor.moveToFirst()) {
                facultyId = cursor.getLong(cursor.getColumnIndex("faculty_id"));
            }
            cursor.close();

            if (facultyId != -1) {
                int rowsDeletedProfessions = db.delete(
                        "Profession",
                        "faculty_id=?",
                        new String[]{String.valueOf(facultyId)}
                );
                int rowsDeleted = db.delete(
                        "Faculty",
                        "faculty_id=?",
                        new String[]{String.valueOf(facultyId)}
                );

                return rowsDeleted;
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    public int updateProfession(String universityName, String facultyName, String updatedProfessionName, String updatedprice,  String professionName, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = -1;
        try {
            String universityQuery = "SELECT university_id FROM University WHERE university_name = ?";
            Cursor universityCursor = db.rawQuery(universityQuery, new String[]{universityName});
            if (universityCursor.moveToFirst()) {
                @SuppressLint("Range") long universityId = universityCursor.getLong(universityCursor.getColumnIndex("university_id"));
                universityCursor.close();

                String facultyQuery = "SELECT faculty_id FROM Faculty WHERE faculty_name = ? AND university_id = ?";
                Cursor facultyCursor = db.rawQuery(facultyQuery, new String[]{facultyName, String.valueOf(universityId)});
                if (facultyCursor.moveToFirst()) {
                    @SuppressLint("Range") long facultyId = facultyCursor.getLong(facultyCursor.getColumnIndex("faculty_id"));
                    facultyCursor.close();

                    String updateQuery = "UPDATE Profession SET profession_name = ?, price = ? " +
                            "WHERE profession_name = ? AND price = ? AND faculty_id = ?";
                    Object[] updateParams = {updatedProfessionName, updatedprice + "AMD", professionName, price, facultyId};
                    db.execSQL(updateQuery, updateParams);
                    rowsAffected = 1;
                }
            }
            universityCursor.close();
        } catch (Exception e) {
            return -1;
        }
        db.close();
        return rowsAffected;
    }

    @SuppressLint("Range")
    public List<University> getAllUniversities() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT University.university_id, university_name, founded, rector, location, phone_number, faculty_name, profession_name, price " +
                "FROM University " +
                "LEFT JOIN Faculty ON University.university_id = Faculty.University_id " +
                "LEFT JOIN Profession ON Faculty.faculty_id = Profession.faculty_id";
        Cursor cursor = db.rawQuery(query, null);

        List<University> universities = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long universityId = cursor.getLong(cursor.getColumnIndex("university_id"));
                University university = null;
                for (University u : universities) {
                    if (u.getId() == universityId) {
                        university = u;
                        break;
                    }
                }

                if (university == null) {
                    university = new University();
                    university.setId(universityId);
                    university.setName(cursor.getString(cursor.getColumnIndex(InfoKeys.UNIVERSITY_NAME.getValue())));
                    university.setFounded(cursor.getString(cursor.getColumnIndex(InfoKeys.FOUNDED_IN.getValue())));
                    university.setRector(cursor.getString(cursor.getColumnIndex(InfoKeys.RECTOR.getValue())));
                    university.setLocation(cursor.getString(cursor.getColumnIndex(InfoKeys.LOCATION.getValue())));
                    university.setPhoneNumber(cursor.getString(cursor.getColumnIndex(InfoKeys.PHONE.getValue())));
                    universities.add(university);
                }

                String facultyName = cursor.getString(cursor.getColumnIndex(InfoKeys.FACULTY_NAME.getValue()));
                Faculty faculty = null;
                for (Faculty f : university.getFaculties()) {
                    if (f.getName().equals(facultyName)) {
                        faculty = f;
                        break;
                    }
                }

                if (faculty == null) {
                    faculty = new Faculty();
                    faculty.setName(facultyName);
                    university.setFaculties(faculty);
                }

                Profession profession = new Profession();
                profession.setName(cursor.getString(cursor.getColumnIndex(InfoKeys.PROFESSION_NAME.getValue())));
                profession.setPrice(cursor.getString(cursor.getColumnIndex(InfoKeys.PRICE.getValue())));
                faculty.setProfessions(profession);

            } while (cursor.moveToNext());
            cursor.close();
        }
        return universities;
    }
    public List<Pair<String, String>> searchProfessionsInUniversities(String searchText) {
        SQLiteDatabase db = getReadableDatabase();
        List<Pair<String, String>> results = new ArrayList<>();

        // Query to search for professions whose names contain the searchText
        String query = "SELECT University.university_name, Faculty.faculty_name, Profession.profession_name, Profession.price " +
                "FROM Profession " +
                "INNER JOIN Faculty ON Profession.faculty_id = Faculty.faculty_id " +
                "INNER JOIN University ON Faculty.University_id = University.university_id " +
                "WHERE Profession.profession_name LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String universityName = cursor.getString(cursor.getColumnIndex("university_name"));
                @SuppressLint("Range") String facultyName = cursor.getString(cursor.getColumnIndex("faculty_name"));
                @SuppressLint("Range") String professionName = cursor.getString(cursor.getColumnIndex("profession_name"));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex("price"));
                String formattedResult = universityName + "\n" +
                        "Ֆակուլտետ: " + facultyName + "\n" +
                        "Մասնագիտություն: " + professionName;

                Pair<String, String> result = new Pair<>(formattedResult, price);
                results.add(result);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Close the database connection
        db.close();

        return results;
    }





    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE University ("
                + "university_id INTEGER PRIMARY KEY,"
                + "university_name TEXT,"
                + "founded TEXT,"
                + "rector TEXT,"
                + "location TEXT,"
                + "phone_number TEXT"
                + ")");


        db.execSQL("CREATE TABLE Faculty ("
                + "faculty_id INTEGER PRIMARY KEY,"
                + "faculty_name TEXT,"
                + "University_id INTEGER,"
                + "FOREIGN KEY (University_id) REFERENCES University(id)"
                + ")");

        db.execSQL("CREATE TABLE Profession ("
                + "profession_id INTEGER PRIMARY KEY,"
                + "profession_name TEXT,"
                + "price TEXT,"
                + "faculty_id INTEGER,"
                + "FOREIGN KEY (faculty_id) REFERENCES Faculty(id)"
                + ")");
    }

    @Override
    public  void onUpgrade (SQLiteDatabase sqLiteDatabase, int a, int b) {

    }
}
