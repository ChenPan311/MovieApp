package com.example.movieapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.movieapp.Models.GenreModel;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.MovieActivity;
import com.example.movieapp.MovieRecyclerView;
import com.example.movieapp.OnMovieListener;
import com.example.movieapp.R;
import com.example.movieapp.Utils.AppExecutors;

import java.util.List;

public class PageFragment extends Fragment implements OnMovieListener {

    public static GenreModel mGenre;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerView;
    private MainViewModel viewModel;
    private int page = 1;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(GenreModel genre) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putSerializable("genre", genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        if (getArguments() != null) {
            mGenre = (GenreModel) getArguments().getSerializable("genre");
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setMoviesDB(MoviesDatabase.getInstance(getContext()));
        viewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for(MovieModel movie : movieModels){
                            if(viewModel.getMoviesDB().movieDao().checkIfMovieIn(movie.getId()))
                                movie.setLiked(true);
                        }
                    }
                });
                if(page == 1) {
                    GridLayoutManager layoutManager
                            = new GridLayoutManager(requireContext(), 2);
                    movieRecyclerView = new MovieRecyclerView(movieModels, PageFragment.this);
                    recyclerView.setAdapter(movieRecyclerView);
                    recyclerView.setLayoutManager(layoutManager);
                }else{
                    int lastSize = movieRecyclerView.getItemCount();
                    movieRecyclerView.addToMovieModelList(movieModels);
                    movieRecyclerView.notifyItemRangeInserted(lastSize,movieRecyclerView.getItemCount());
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    viewModel.getMoreMoviesByGenre(String.valueOf(mGenre.getId()),String.valueOf(page));
                }
            }
        });

        viewModel.getMoviesByGenre(String.valueOf(mGenre.getId()));
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

    @Override
    public void onLikeClick(int position) {
        MovieModel model = movieRecyclerView.getSelectedMovie(position);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (model.isLiked()) {
                    model.setLiked(!model.isLiked());
                    viewModel.getMoviesDB().movieDao().deleteMovie(model);
                }
                else {
                    model.setLiked(!model.isLiked());
                    viewModel.getMoviesDB().movieDao().addMovie(model);
                }
            }
        });
        movieRecyclerView.notifyItemChanged(position);
    }
}