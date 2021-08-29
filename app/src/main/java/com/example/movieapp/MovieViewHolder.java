package com.example.movieapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title, duration, rating;
    ImageView image;
    RatingBar ratingBar;
    private OnMovieListener movieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener movieListener) {
        super(itemView);
        title = itemView.findViewById(R.id.movie_title);
        duration = itemView.findViewById(R.id.movie_duration);
        rating = itemView.findViewById(R.id.movie_rating_tv);
        image = itemView.findViewById(R.id.movie_image);
        ratingBar = itemView.findViewById(R.id.movie_rating_rb);
        this.movieListener = movieListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        movieListener.onMovieClick(getAdapterPosition(),image);
    }
}
