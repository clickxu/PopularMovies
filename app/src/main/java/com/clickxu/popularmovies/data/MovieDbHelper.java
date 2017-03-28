package com.clickxu.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.clickxu.popularmovies.data.MovieContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by t-xu on 3/15/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "com.clickxu.popularmovies";
    private static final int DB_VERSION = 20170328;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" + MovieContract.FavoriteEntry._ID +
                " INTEGER PRIMARY KEY, " +
                MovieContract.FavoriteEntry.TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.FavoriteEntry.OVERVIEW +
                " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        // re-create database
        onCreate(db);
    }
}
