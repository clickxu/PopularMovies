package com.clickxu.popularmovies;

import android.content.ContentResolver;

import com.clickxu.popularmovies.data.MovieRepository;

/**
 * Created by t-xu on 2/27/17.
 */

public class Injection {

    public static MovieRepository provideMovieRepository(ContentResolver contentResolver) {
        return MovieRepository.getInstance(contentResolver);
    }
}
