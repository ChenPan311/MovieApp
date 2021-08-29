package com.example.movieapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Request.Service;
import com.example.movieapp.Response.MovieSearchResponse;
import com.example.movieapp.Utils.Comparator;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<MovieModel>> moviesList;
    private final Callback<MovieSearchResponse> callback = new Callback<MovieSearchResponse>() {
        @Override
        public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
            if (response.code() == 200) {
                List<MovieModel> movies = new ArrayList<>(response.body().getMovieList());
                Collections.sort(movies, new Comparator.RatingComparator(-1));
                moviesList.setValue(movies);
            } else {
                try {
                    Log.v("Tag", "Error " + response.errorBody().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

        }
    };

    public LiveData<List<MovieModel>> getMovies(){
        if (moviesList == null){
            moviesList = new MutableLiveData<>();
        }
        return moviesList;
    }


    public void getPopularMovies() {
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.getPopular(Credentials.API_KEY, "1");
        responseCall.enqueue(callback);
    }

    public void getRetrofitResponse(String query) {
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                query,
                "1"
        );

        responseCall.enqueue(callback);
    }
}
