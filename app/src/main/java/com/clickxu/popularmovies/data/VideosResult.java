
package com.clickxu.popularmovies.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosResult implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Video> mVideos = null;
    public final static Creator<VideosResult> CREATOR = new Creator<VideosResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public VideosResult createFromParcel(Parcel in) {
            VideosResult instance = new VideosResult();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.mVideos, (Video.class.getClassLoader()));
            return instance;
        }

        public VideosResult[] newArray(int size) {
            return (new VideosResult[size]);
        }

    }
    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        this.mVideos = videos;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(mVideos);
    }

    public int describeContents() {
        return  0;
    }

}
