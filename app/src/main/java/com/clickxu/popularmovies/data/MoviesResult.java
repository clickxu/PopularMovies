
package com.clickxu.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResult implements Parcelable {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    public final static Creator<MoviesResult> CREATOR = new Creator<MoviesResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MoviesResult createFromParcel(Parcel in) {
            MoviesResult instance = new MoviesResult();
            instance.page = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.movies, (Movie.class.getClassLoader()));
            instance.totalResults = ((int) in.readValue((int.class.getClassLoader())));
            instance.totalPages = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MoviesResult[] newArray(int size) {
            return (new MoviesResult[size]);
        }

    }
    ;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(movies);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return  0;
    }

}
