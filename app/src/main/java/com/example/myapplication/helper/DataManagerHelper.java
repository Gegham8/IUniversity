package com.example.myapplication.helper;

import com.example.myapplication.models.Faculty;
import com.example.myapplication.models.University;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataManagerHelper {
    private static ArrayList<University> universitiesData;
    private static final Map<String, Faculty> facultyData = new HashMap<>();
    public static void setUniversitiesData(ArrayList<University> data) {
        universitiesData = data;
    }
    private static String universityName;
    public static ArrayList<University> getUniversitiesData() {
        return universitiesData;
    }
    public static University getUniversityByName(String name) {
        if (universitiesData != null) {
            for (University university : universitiesData) {
                if (university.getName().equals(name)) {
                    return university;
                }
            }
        }
        return null;
    }

    public static void setFaculty(String universityName, String facultyName) {
        University university = getUniversityByName(universityName);
        if (university != null) {
            Faculty faculty = findFacultyInUniversity(university, facultyName);
            if (faculty != null) {
                facultyData.put(facultyName, faculty);
            }
        }
    }

    public static Faculty getFacultyByName(String facultyName) {
        return facultyData.get(facultyName);
    }

    public static String getUniversityName () {
        return universityName;
    }
    private static Faculty findFacultyInUniversity(University university, String facultyName) {
        if (university != null) {
            for (Faculty faculty : university.getFaculties()) {
                if (faculty.getName().equals(facultyName)) {
                    universityName = university.getName();
                    return faculty;
                }
            }
        }
        return null;
    }
}
