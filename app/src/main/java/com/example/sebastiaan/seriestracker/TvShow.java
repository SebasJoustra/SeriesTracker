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

    public TvShow(String aName, String anId) {
        this.name = aName;
        this.id = anId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}