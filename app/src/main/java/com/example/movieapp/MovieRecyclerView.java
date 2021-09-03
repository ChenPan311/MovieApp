package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.movieapp.Models.MovieModel;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<MovieViewHolder> {
    private List<MovieModel> movieModelList;
    private OnMovieListener movieListener;

    public MovieRecyclerView(List<MovieModel> movieModelList) {
        this.movieModelList = movieModelList;
    }

    public MovieRecyclerView(List<MovieModel> movieModelList, OnMovieListener movieListener) {
        this.movieModelList = movieModelList;
        this.movieListener = movieListener;
    }

    public void addToMovieModelList(List<MovieModel> movieModelList) {
        this.movieModelList.addAll(movieModelList);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item ,parent, false);
        return new MovieViewHolder(view, movieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieModelList.get(position);
        holder.title.setText(movie.getTitle());
        holder.duration.setText(movie.getRelease_date());
        holder.rating.setText(movie.getVote_average());
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500/"+movie.getPoster_path())
                .transform(new CenterCrop(),new RoundedCorners(15))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    public MovieModel getSelectedMovie(int position) {
        return movieModelList.get(position);
    }
}
