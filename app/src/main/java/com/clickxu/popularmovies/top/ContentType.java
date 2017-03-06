package com.clickxu.popularmovies.top;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by t-xu on 2/14/17.
 */
@Retention(SOURCE)
@IntDef({POP_MOViES, TOP_RATED_MOViES})
public @interface ContentType {
    int POP_MOViES = 0;
    int TOP_RATED_MOViES = 1;
}
