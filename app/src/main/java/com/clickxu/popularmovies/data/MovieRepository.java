package com.clickxu.popularmovies.data;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

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
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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
    public Observable<MoviesResult> getPopularMovies(int page) {
        return mMovieDataSource.getPopularMovies(page);
    }

    @Override
    public Observable<MoviesResult> getTopRatedMovies(int page) {
        return mMovieDataSource.getTopRatedMovies(page);
    }

    @Override
    public Observable<VideosResult> getVideos(String movieId) {
        return mMovieDataSource.getVideos(movieId);
    }

    @Override
    public Observable<ReviewsResult> getReviews(String movieId) {
        return mMovieDataSource.getReviews(movieId);
    }
}
