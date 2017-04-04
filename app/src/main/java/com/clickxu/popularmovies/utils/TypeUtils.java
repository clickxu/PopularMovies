package com.clickxu.popularmovies.utils;

import com.clickxu.popularmovies.R;
import com.clickxu.popularmovies.top.ContentType;

import static com.clickxu.popularmovies.top.ContentType.FAVORITE_MOViES;
import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;

/**
 * Created by t-xu on 2/14/17.
 */

public class TypeUtils {

    private TypeUtils() {
    }

    @ContentType
    public static int getContentType(int i) {
        if (i != POP_MOViES && i != TOP_RATED_MOViES && i != FAVORITE_MOViES) {
            throw new IllegalArgumentException("Unknown ContentType: " + i);
        }
        return i;
    }

    public static int getTitle(@ContentType int contentType) {
        switch (contentType) {
            case POP_MOViES:
                return R.string.title_pop;
            case TOP_RATED_MOViES:
                return R.string.title_rate;
            case FAVORITE_MOViES:
                return R.string.title_favorite;
            default:
                throw new IllegalArgumentException("Unknown ContentType: " + contentType);
        }
    }
}
