
package com.clickxu.popularmovies.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewsResult implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Review> mReviews = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    public final static Creator<ReviewsResult> CREATOR = new Creator<ReviewsResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ReviewsResult createFromParcel(Parcel in) {
            ReviewsResult instance = new ReviewsResult();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.page = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.mReviews, (Review.class.getClassLoader()));
            instance.totalPages = ((int) in.readValue((int.class.getClassLoader())));
            instance.totalResults = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public ReviewsResult[] newArray(int size) {
            return (new ReviewsResult[size]);
        }

    }
    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(mReviews);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return  0;
    }

}
