package com.example.movieapp.Utils;

import androidx.annotation.Keep;

import com.example.movieapp.Response.GenreResponse;
import com.example.movieapp.Response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );

    @GET("movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("movie/now_playing")
    Call<MovieSearchResponse> getNowPlaying(
            @Query("api_key") String key,
            @Query("page") String page
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String key
    );

    @GET("discover/movie")
    Call<MovieSearchResponse> getMoviesByGenre(
            @Query("api_key") String key,
            @Query("page") String page,
            @Query("with_genres") String genres
    );
}
