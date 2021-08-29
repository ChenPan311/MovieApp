package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Request.Service;
import com.example.movieapp.Response.MovieSearchResponse;
import com.example.movieapp.Utils.Comparator;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerView;
    private TextView resultsTv;
    private SearchView searchView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel= new ViewModelProvider(this).get(MainViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        resultsTv = findViewById(R.id.results_tv);
        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.progressBar);

        mainViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels.isEmpty()) {
                    resultsTv.setVisibility(View.VISIBLE);
                } else {
                    resultsTv.setVisibility(View.GONE);
                }
                movieRecyclerView = new MovieRecyclerView(movieModels, MovieListActivity.this);
                recyclerView.setAdapter(movieRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(MovieListActivity.this));
                progressBar.setVisibility(View.GONE);
            }
        });

        mainViewModel.getPopularMovies();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(""))
                    mainViewModel.getPopularMovies();
                else
                    mainViewModel.getRetrofitResponse(s);
                return false;
            }
        });
    }


    @Override
    public void onMovieClick(int position, ImageView img) {
        Intent intent = new Intent(MovieListActivity.this, MovieActivity.class);
        intent.putExtra("movie", movieRecyclerView.getSelectedMovie(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(MovieListActivity.this, img, "imageAnim");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}