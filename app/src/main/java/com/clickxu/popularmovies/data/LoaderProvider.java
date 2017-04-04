package com.clickxu.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by t-xu on 3/27/17.
 */

public class LoaderProvider {

    @NonNull
    private final Context mContext;

    public LoaderProvider(@NonNull Context context) {
        mContext = checkNotNull(context, "Context cannot be null");
    }

    public Loader<Cursor> createFavoriteMoviesLoader() {
        return new CursorLoader(mContext,
                MovieContract.FavoriteEntry.CONTENT_URI,
                null, null, null, MovieContract.FavoriteEntry._ID + " ASC");
    }
}
