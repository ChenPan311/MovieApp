package com.example.movieapp.Models;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.movieapp.Fragments.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentStateAdapter {
    private List<GenreModel> genresList = new ArrayList<>();

    public PageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public PageAdapter(@NonNull Fragment fragment, List<GenreModel> genres) {
        super(fragment);
        this.genresList = genres;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PageFragment.newInstance(genresList.get(position));
    }


    @Override
    public int getItemCount() {
        return genresList.size();
    }


}
