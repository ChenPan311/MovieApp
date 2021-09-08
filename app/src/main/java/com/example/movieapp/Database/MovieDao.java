package com.example.movieapp.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.Models.MovieModel;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("Select * from movies")
    LiveData<List<MovieModel>> getMoviesList();
    @Insert
    void addMovie(MovieModel movie);
    @Update
    void updateMovie(MovieModel movie);
    @Delete
    void deleteMovie(MovieModel movie);
}
