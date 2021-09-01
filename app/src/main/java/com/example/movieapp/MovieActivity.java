package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Models.MovieModel;

public class MovieActivity extends AppCompatActivity {
    private ImageView movieImage;
    private TextView movieTitle, movieRating, movieOverview;
    private RatingBar movieRatingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieImage = findViewById(R.id.movie_image);
        movieOverview = findViewById(R.id.moview_overview);
        movieRating = findViewById(R.id.movie_rating_tv);
        movieRatingbar = findViewById(R.id.movie_rating_rb);
        movieTitle = findViewById(R.id.movie_title);

        if (getIntent().hasExtra("movie")) {
            MovieModel movieModel = getIntent().getParcelableExtra("movie");
            movieTitle.setText(movieModel.getTitle());
            movieOverview.setText(movieModel.getOverview());
            movieRating.setText(movieModel.getVote_average());
            movieRatingbar.setRating(Float.parseFloat(movieModel.getVote_average()));
            Glide.with(MovieActivity.this).load("https://image.tmdb.org/t/p/w500/"
                    + movieModel.getPoster_path()).into(movieImage);
        }
    }

}