package com.softed.android.popularmovies.Utilities;

/**
 * Created by Edi on 28.01.2017.
 */

public class Season {
    private String ID;
    private String airDate;
    private int episodesCount;
    private int number;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
