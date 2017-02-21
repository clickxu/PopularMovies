package com.clickxu.popularmovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by t-xu on 2/21/17.
 */

public class DisplayUtils {

    private DisplayUtils() {
    }

    public static int calculateNoOfColumns(Context context, int contentWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / contentWidthDp);
    }
}
