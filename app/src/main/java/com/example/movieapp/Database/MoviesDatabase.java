package com.example.movieapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movieapp.Models.MovieModel;

@Database(entities = MovieModel.class, exportSchema = false,version = 2)
public abstract class MoviesDatabase extends RoomDatabase {
    private static final String DB_NAME = "movies_db";
    private static MoviesDatabase instance;

    public static synchronized MoviesDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MoviesDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract MovieDao movieDao();
}
