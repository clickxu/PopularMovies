package com.clickxu.popularmovies.data;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.clickxu.popularmovies.BuildConfig.MOVIE_URL;

/**
 * Created by t-xu on 2/2/17.
 */

public class MovieRepository implements MovieDataSource {

    private static final OkHttpClient sOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(70, TimeUnit.SECONDS)
            .addInterceptor(
                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build();
    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .client(sOkHttpClient)
            .baseUrl(MOVIE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    private MovieDataSource mMovieDataSource;

    private static MovieRepository sInstance;

    private MovieRepository() {
        mMovieDataSource = sRetrofit.create(MovieDataSource.class);
    }

    public static MovieRepository getInstance() {
        if (sInstance == null) {
            sInstance = new MovieRepository();
        }
        return sInstance;
    }


    @Override
    public Single<MoviesResult> getPopularMovies(int page) {
        return mMovieDataSource.getPopularMovies(page);
    }

    @Override
    public Single<MoviesResult> getTopRatedMovies(int page) {
        return mMovieDataSource.getTopRatedMovies(page);
    }

    @Override
    public Single<VideosResult> getVideos(int movieId) {
        return mMovieDataSource.getVideos(movieId);
    }

    @Override
    public Single<ReviewsResult> getReviews(int movieId) {
        return mMovieDataSource.getReviews(movieId);
    }
}
