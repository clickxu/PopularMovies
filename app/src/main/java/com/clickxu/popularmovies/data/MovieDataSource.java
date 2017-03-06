package com.clickxu.popularmovies.data;

import com.clickxu.popularmovies.BuildConfig;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by t-xu on 2/2/17.
 */

public interface MovieDataSource {

    @GET("popular?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResult> getPopularMovies(@Query("page") int page);

    @GET("top_rated?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResult> getTopRatedMovies(@Query("page") int page);

    @GET("{movie_id}/videos?api_key=" + BuildConfig.API_KEY)
    Observable<VideosResult> getVideos(@Path("movie_id") String movieId);

    @GET("{movie_id}/reviews?api_key=" + BuildConfig.API_KEY)
    Observable<ReviewsResult> getReviews(@Path("movie_id") String movieId);

}
