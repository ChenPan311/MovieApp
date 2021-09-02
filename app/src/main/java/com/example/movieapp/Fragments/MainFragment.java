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

import com.example.movieapp.MainViewModel;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.MovieActivity;
import com.example.movieapp.MovieListActivity;
import com.example.movieapp.MovieRecyclerView;
import com.example.movieapp.OnMovieListener;
import com.example.movieapp.R;

import java.util.List;

public class MainFragment extends Fragment implements OnMovieListener {
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerView;
    private TextView resultsTv;
    private ProgressBar progressBar;


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
        progressBar = view.findViewById(R.id.progressBar);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        mainViewModel= new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels.isEmpty()) {
                    resultsTv.setVisibility(View.VISIBLE);
                } else {
                    resultsTv.setVisibility(View.GONE);
                }
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                movieRecyclerView = new MovieRecyclerView(movieModels, MainFragment.this);
                recyclerView.setAdapter(movieRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                progressBar.setVisibility(View.GONE);
            }
        });

        mainViewModel.getNowPlaying();
        return view;
    }

    @Override
    public void onMovieClick(int position, ImageView img) {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, MovieFragment.newInstance(movieRecyclerView.getSelectedMovie(position)))
//                .addToBackStack("MovieFrag")
//                .commit();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu,menu);
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
                if (s.equals(""))
                    mainViewModel.getNowPlaying();
                else
                    mainViewModel.getRetrofitResponse(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}