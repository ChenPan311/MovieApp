package com.example.movieapp.Response;

import com.example.movieapp.Models.GenreModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GenreResponse {
    @SerializedName("genres")
    @Expose
    private List<GenreModel> genreList;

    private List<String> genreNames = new ArrayList<>();
    private List<Integer> genreIds = new ArrayList<>();

    public List<GenreModel> getGenreList() {
        return genreList;
    }

    public List<String> getGenreNames() {
        genreNames.clear();
        for(GenreModel genreModel : genreList){
            genreNames.add(genreModel.getName());
        }
        return genreNames;
    }

    public List<Integer> getGenreIds() {
        genreIds.clear();
        for(GenreModel genreModel : genreList){
            genreIds.add(genreModel.getId());
        }
        return genreIds;
    }
}
