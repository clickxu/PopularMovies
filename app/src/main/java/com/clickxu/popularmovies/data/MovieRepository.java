package com.clickxu.popularmovies.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
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
    private ContentResolver mContentResolver;

    private static MovieRepository sInstance;

    private MovieRepository(ContentResolver contentResolver) {
        mMovieDataSource = sRetrofit.create(MovieDataSource.class);
        mContentResolver = contentResolver;
    }

    public static MovieRepository getInstance(@NonNull ContentResolver contentResolver) {
        if (sInstance == null) {
            sInstance = new MovieRepository(contentResolver);
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

    public Single<Boolean> isFavorite(Movie movie) {
        return Single.defer(() -> Single.just(checkIsFavorite(movie)));
    }

    private boolean checkIsFavorite(Movie movie) {
        Cursor cursor = mContentResolver.query(
                MovieContract.FavoriteEntry.CONTENT_URI,
                null,
                MovieContract.FavoriteEntry.ID + " = ?",
                new String[]{Integer.toString(movie.getId())},
                null);
        boolean isFavorite = false;
        if (cursor != null) {
            isFavorite = cursor.getCount() > 0;
            cursor.close();
        }
        return isFavorite;
    }

    public Single<Boolean> setFavorite(Movie movie, boolean isFavorite) {
        return Single.defer(() -> Single.just(favorite(movie, isFavorite)));
    }

    private boolean favorite(Movie movie, boolean isFavorite) {
        if (isFavorite) {
            Uri uri = mContentResolver.insert(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.buildContentValueFrom(movie));
            return uri != null;
        } else {
            mContentResolver.delete(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.ID + " = ?",
                    new String[]{"" + movie.getId()}
            );
            return false;
        }
    }
}
