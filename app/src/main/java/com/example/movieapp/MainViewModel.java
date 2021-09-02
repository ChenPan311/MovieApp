package com.example.movieapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.Models.GenreModel;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Request.Service;
import com.example.movieapp.Response.GenreResponse;
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
    private MutableLiveData<List<GenreModel>> genresList;
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

    public LiveData<List<GenreModel>> getGenres(){
        if (genresList == null){
            genresList = new MutableLiveData<>();
        }
        return genresList;
    }


    public void getPopularMovies() {
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.getPopular(Credentials.API_KEY, "1");
        responseCall.enqueue(callback);
    }

    public void getNowPlaying(){
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.getNowPlaying(Credentials.API_KEY,"1");
        responseCall.enqueue(callback);
    }

    public void getMoviesByGenre(String genreId){
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.getMoviesByGenre(Credentials.API_KEY,"1",genreId);
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

    public void getGenresList(){
        MovieApi movieApi = Service.getMovieApi();
        Call<GenreResponse> responseCall = movieApi.getGenres(Credentials.API_KEY);
        responseCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                List<GenreModel> list = new ArrayList<>(response.body().getGenreList());
                genresList.setValue(list);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {

            }
        });
    }
}
