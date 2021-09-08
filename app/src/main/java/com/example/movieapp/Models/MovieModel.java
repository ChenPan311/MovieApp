package com.example.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class MovieModel implements Parcelable {
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    @PrimaryKey
    private int id;
    private String overview;
    private String vote_average;
    private boolean isLiked = false;

    public MovieModel(String title, String poster_path, String backdrop_path, String release_date, int id, String overview, String vote_average) {
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
        this.id = id;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    @Ignore
    protected MovieModel(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        id = in.readInt();
        overview = in.readString();
        vote_average = in.readString();
    }

    @Ignore
    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeInt(id);
        parcel.writeString(overview);
        parcel.writeString(vote_average);
    }
}
