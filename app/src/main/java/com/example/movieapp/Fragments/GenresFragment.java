package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.MainViewModel;
import com.example.movieapp.Models.GenreModel;
import com.example.movieapp.Models.PageAdapter;
import com.example.movieapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class GenresFragment extends Fragment {
    private MainViewModel viewModel;
    private PageAdapter pageAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    public GenresFragment() {
        // Required empty public constructor
    }

    public static GenresFragment newInstance() {
        return new GenresFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);
        viewModel= new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getGenres().observe(getViewLifecycleOwner(), new Observer<List<GenreModel>>() {
            @Override
            public void onChanged(List<GenreModel> genreModels) {
                pageAdapter = new PageAdapter(GenresFragment.this,genreModels);
                viewPager.setAdapter(pageAdapter);
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText(genreModels.get(position).getName())
                ).attach();
            }
        });
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setSaveEnabled(false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewModel.getGenresList();

        return view;
    }
}