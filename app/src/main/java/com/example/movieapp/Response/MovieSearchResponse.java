package com.example.movieapp.Response;

import com.example.movieapp.Models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResponse {
    @SerializedName("total_results")
    @Expose()
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    @SerializedName("results")
    @Expose()
    private List<MovieModel> movieList;

    public List<MovieModel> getMovieList(){
        return movieList;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "totalCount=" + totalCount +
                ", movieList=" + movieList +
                '}';
    }
}
