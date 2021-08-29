package com.example.movieapp.Utils;

import com.example.movieapp.Models.MovieModel;

public class Comparator {

    public static class RatingComparator implements java.util.Comparator<MovieModel> {
        private int order = 1;

        public RatingComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(MovieModel movieModel, MovieModel t1) {
            return order * Double.compare(Double.parseDouble(movieModel.getVote_average())
                    , Double.parseDouble(t1.getVote_average()));
        }
    }

    public static class TitleComparator implements java.util.Comparator<MovieModel> {
        private int order = 1;

        public TitleComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(MovieModel movieModel, MovieModel t1) {
            return order * movieModel.getTitle().compareTo(t1.getTitle());
        }
    }
}
