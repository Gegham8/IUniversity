package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class University implements Parcelable {
    private long id;
    private String name;
    private String founded;
    private String rector;
    private String location;
    private Integer imageResId;

    private String phoneNumber;
    private final List<Faculty> faculties;

    public University () {
        this.faculties = new ArrayList<>();
    }
    public University(String name, int imageResId, String foundedIn,  String location, String phone,
                      String rector, List<Faculty> faculties) {
        this.name = name;
        this.imageResId = imageResId;
        this.founded = foundedIn;
        this.location = location;
        this.phoneNumber = phone;
        this.rector = rector;
        this.faculties = faculties;
    }
    protected University(Parcel in) {
        id = in.readLong();
        name = in.readString();
        founded = in.readString();
        rector = in.readString();
        location = in.readString();
        imageResId = in.readInt();
        phoneNumber = in.readString();
        faculties = new ArrayList<>();
        in.readList(faculties, Faculty.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(founded);
        dest.writeString(rector);
        dest.writeString(location);
        if (imageResId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imageResId);
        }
        dest.writeString(phoneNumber);
        dest.writeList(faculties);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<University> CREATOR = new Creator<University>() {
        @Override
        public University createFromParcel(Parcel in) {
            return new University(in);
        }

        @Override
        public University[] newArray(int size) {
            return new University[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setImageResId(Integer imageResId) {
        this.imageResId = imageResId;
    }

    public Integer getImageResId () {
        return  this.imageResId;
    }

    public void setUniversityDetails(String name, String founded, String rector, String foreignStudents,
                                     String bachelors, String masters, String postgraduate,
                                     String professor, String teachers, String location, String phoneNumber) {
        this.setName(name);
        this.setFounded(founded);
        this.setRector(rector);
        this.setLocation(location);
        this.setPhoneNumber(phoneNumber);
    }



    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getRector() {
        return rector;
    }

    public void setRector(String rector) {
        this.rector = rector;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(Faculty faculty) {
        this.faculties.add(faculty);
    }
}