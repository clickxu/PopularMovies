package com.clickxu.popularmovies.utils;

import com.clickxu.popularmovies.top.ContentType;

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
        if (i != POP_MOViES && i != TOP_RATED_MOViES) {
            i = POP_MOViES;
        }
        return i;
    }
}
