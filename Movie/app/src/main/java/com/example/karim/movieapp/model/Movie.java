package com.example.karim.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public int id;
    public String title;
    public String image;
    public String overview;
    public String rate;
    public String releaseDate;

    //constructor to save data
    public Movie(int id , String title, String image, String overview, String rate, String releaseDate) {
        this.id = id;  //0
        this.title = title;//1
        this.image = image;//2
        this.overview = overview;//5
        this.rate = rate;//3
        this.releaseDate = releaseDate;//4
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        overview = in.readString();
        rate = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(overview);
        parcel.writeString(rate);
        parcel.writeString(releaseDate);
    }
}

