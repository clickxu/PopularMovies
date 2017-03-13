package com.clickxu.popularmovies.data;

import com.clickxu.popularmovies.BuildConfig;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by t-xu on 2/2/17.
 */

interface MovieDataSource {

    @GET("popular?api_key=" + BuildConfig.API_KEY)
    Single<MoviesResult> getPopularMovies(@Query("page") int page);

    @GET("top_rated?api_key=" + BuildConfig.API_KEY)
    Single<MoviesResult> getTopRatedMovies(@Query("page") int page);

    @GET("{movie_id}/videos?api_key=" + BuildConfig.API_KEY)
    Single<VideosResult> getVideos(@Path("movie_id") int movieId);

    @GET("{movie_id}/reviews?api_key=" + BuildConfig.API_KEY)
    Single<ReviewsResult> getReviews(@Path("movie_id") int movieId);

}
