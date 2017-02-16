package com.clickxu.popularmovies.datasource;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by t-xu on 2/2/17.
 */

public interface MovieDataSource {

    @GET("popular")
    Call<MoviesResult> getPopularMovies(@QueryMap Map<String, String> options);

    @GET("top_rated")
    Call<MoviesResult> getTopRatedMovies(@QueryMap Map<String, String> options);
}
