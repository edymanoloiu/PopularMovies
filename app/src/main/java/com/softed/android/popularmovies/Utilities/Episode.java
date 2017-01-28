package com.softed.android.popularmovies.Utilities;

/**
 * Created by Edi on 28.01.2017.
 */

public class Episode {
    private String air_date;
    private String name;
    private String overview;
    private String ID;
    private String posterPath;
    private String rating;

    public String getOverview() {
        return overview;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getAir_date() {
        return air_date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getRating() {
        return rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
