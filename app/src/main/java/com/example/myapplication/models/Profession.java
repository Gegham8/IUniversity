package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Profession implements Parcelable {
    private long id; // Assuming you have an ID field for database operations
    private String name;
    private String price;

    public Profession() {
        // Default constructor
    }
    public Profession(String name, String price) {
        this.name = name;
        this.price = price;
    }
    protected Profession(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profession> CREATOR = new Creator<Profession>() {
        @Override
        public Profession createFromParcel(Parcel in) {
            return new Profession(in);
        }

        @Override
        public Profession[] newArray(int size) {
            return new Profession[size];
        }
    };


    // Getters and setters
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
