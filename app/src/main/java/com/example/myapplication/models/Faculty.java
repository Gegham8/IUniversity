package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Faculty implements Parcelable {
    private long id;
    private String name;
    private final List<Profession> professions;

    public Faculty() {
        this.professions = new ArrayList<>();
    }
    public Faculty(String name, List<Profession> professions) {
        this.name = name;
        this.professions = professions;
    }

    protected Faculty(Parcel in) {
        id = in.readLong();
        name = in.readString();
        professions = in.createTypedArrayList(Profession.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(professions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Faculty> CREATOR = new Creator<Faculty>() {
        @Override
        public Faculty createFromParcel(Parcel in) {
            return new Faculty(in);
        }

        @Override
        public Faculty[] newArray(int size) {
            return new Faculty[size];
        }
    };

    public long getId() {
        return id;
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

    public List<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(Profession profession) {
        this.professions.add(profession);
    }
}
