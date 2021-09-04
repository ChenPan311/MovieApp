package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.MainViewModel;
import com.example.movieapp.R;

public class MyMoviesFragment extends Fragment {
    MainViewModel viewModel;

    public MyMoviesFragment() {
        // Required empty public constructor
    }

    public static MyMoviesFragment newInstance(String param1, String param2) {
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
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        return view;
    }
}