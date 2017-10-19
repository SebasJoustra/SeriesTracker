package com.example.sebastiaan.seriestracker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Tv-show model class
 */

public class TvShow implements Serializable{

    public String name;
    public String id;
    public String image;
    public String voteAvg;
    public String description;
    public int numOfEpisodes;
    public int numOfSeasons;
    public ArrayList<ArrayList<Boolean>> episodesCompletedList;

    public TvShow() {}

    public TvShow(String aName, String anId, String anImage, String aDescription, String aVoteAvg) {
        this.name = aName;
        this.id = anId;
        this.image = anImage;
        this.description = aDescription;
        this.voteAvg = aVoteAvg;
    }

    public int getNumOfEpisodes() {
        return numOfEpisodes;
    }

    public void setNumOfEpisodes(int numOfEpisodes) {
        this.numOfEpisodes = numOfEpisodes;
    }

    public int getNumOfSeasons() {
        return numOfSeasons;
    }

    public void setNumOfSeasons(int numOfSeasons) {
        this.numOfSeasons = numOfSeasons;
    }

    public ArrayList<ArrayList<Boolean>> getEpisodesCompletedList() {
        return episodesCompletedList;
    }

    public void setEpisodesCompletedList(ArrayList<ArrayList<Boolean>> episodesCompletedList) {
        this.episodesCompletedList = episodesCompletedList;
    }
}