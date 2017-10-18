package com.example.sebastiaan.seriestracker;


import java.util.ArrayList;
import java.util.List;

public class TvShows {

    List<TvShow> tvShows = new ArrayList<>();

    public TvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    public List<TvShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    public void addTvShow(TvShow tvShow) {
        this.tvShows.add(tvShow);
    }
}
