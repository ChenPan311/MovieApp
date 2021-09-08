package com.example.movieapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.movieapp.Database.MoviesDatabase;
import com.example.movieapp.MainViewModel;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.MovieActivity;
import com.example.movieapp.MovieRecyclerView;
import com.example.movieapp.OnMovieListener;
import com.example.movieapp.R;

import java.util.List;

public class MyMoviesFragment extends Fragment implements OnMovieListener {
    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerView;

    public MyMoviesFragment() {
        // Required empty public constructor
    }

    public static MyMoviesFragment newInstance() {
        return new MyMoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_movies, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.setMoviesDB(MoviesDatabase.getInstance(getContext()));
        List<MovieModel> list = viewModel.getMoviesDB().movieDao().getMoviesList();

        viewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                GridLayoutManager layoutManager
                        = new GridLayoutManager(requireContext(), 2);
                movieRecyclerView = new MovieRecyclerView(movieModels, MyMoviesFragment.this);
                recyclerView.setAdapter(movieRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
            }
        });
        return view;
    }

    @Override
    public void onMovieClick(int position, ImageView img) {
        Intent intent = new Intent(getContext(), MovieActivity.class);
        intent.putExtra("movie", movieRecyclerView.getSelectedMovie(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), img, "imageAnim");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}