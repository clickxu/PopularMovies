package com.clickxu.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by t-xu on 3/16/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.clickxu.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite_movies";

        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String ID = "id";
        public static final String RELEASE_DATE = "release_date";
        public static final String TITLE = "title";
        public static final String VOTE_AVERAGE = "vote_average";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        public static ContentValues buildContentValueFrom(Movie movie) {
            ContentValues values = new ContentValues();
            values.put(POSTER_PATH, movie.getPosterPath());
            values.put(OVERVIEW, movie.getOverview());
            values.put(ID, movie.getId());
            values.put(RELEASE_DATE, movie.getReleaseDate());
            values.put(TITLE, movie.getTitle());
            values.put(VOTE_AVERAGE, movie.getVoteAverage());
            return values;
        }

    }
}
