package com.example.myapplication.helper;

import android.content.Context;
import android.content.res.Resources;

import com.example.myapplication.models.University;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
public class UniversityDataReader {

    public static List<University> readUniversityDataFromJson(Context context, int resourceId) {
        Gson gson = new Gson();
        List<University> universityList = null;

        try {
            Resources resources = context.getResources();
            InputStream inputStream = ((Resources) resources).openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            UniversityData universityData = gson.fromJson(inputStreamReader, UniversityData.class);
            universityList = universityData.getUniversities();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return universityList;
    }
}
