package com.example.movieapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.Database.MoviesDatabase;
import com.example.movieapp.MainViewModel;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.MovieActivity;
import com.example.movieapp.MovieListActivity;
import com.example.movieapp.MovieRecyclerView;
import com.example.movieapp.OnMovieListener;
import com.example.movieapp.R;
import com.example.movieapp.Utils.AppExecutors;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class MainFragment extends Fragment implements OnMovieListener {
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerView;
    private TextView resultsTv, nowPlayingTv;
    private ShimmerFrameLayout shimmerFrameLayout;


    public MainFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        resultsTv = view.findViewById(R.id.results_tv);
        nowPlayingTv = view.findViewById(R.id.now_playing_tv);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);

        shimmerFrameLayout.startShimmer();
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setMoviesDB(MoviesDatabase.getInstance(getContext()));
        mainViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels.isEmpty()) {
                    resultsTv.setVisibility(View.VISIBLE);
                } else {
                    resultsTv.setVisibility(View.GONE);
                }
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for(MovieModel movie : movieModels){
                            if(mainViewModel.getMoviesDB().movieDao().checkIfMovieIn(movie.getId()))
                                movie.setLiked(true);
                        }
                    }
                });
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                movieRecyclerView = new MovieRecyclerView(movieModels, MainFragment.this);
                recyclerView.setAdapter(movieRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        mainViewModel.getNowPlaying();
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
        //TODO: check if movie already added
        MovieModel model = mainViewModel.getMovies().getValue().get(position);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (model.isLiked()) {
                    model.setLiked(!model.isLiked());
                    mainViewModel.getMoviesDB().movieDao().deleteMovie(model);
                }
                else {
                    model.setLiked(!model.isLiked());
                    mainViewModel.getMoviesDB().movieDao().addMovie(model);
                }
            }
        });
        movieRecyclerView.notifyItemChanged(position);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter Movie Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    nowPlayingTv.setText("Now Playing");
                    mainViewModel.getNowPlaying();
                } else {
                    nowPlayingTv.setText("Search Results");
                    mainViewModel.getRetrofitResponse(s);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}