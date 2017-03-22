package com.clickxu.popularmovies.detail;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.clickxu.popularmovies.data.Movie;
import com.clickxu.popularmovies.data.MovieRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by t-xu on 2/26/17.
 */

public class DetailPresenter implements DetailContract.Presenter {

    final private Movie mMovie;
    final private MovieRepository mMovieRepository;
    private LoaderManager mLoaderManager;
    private DetailContract.View mView;
    private CompositeDisposable mDisposables;
    private boolean mIsFavorite = false;

    DetailPresenter(@NonNull Movie movie,
                    //@NonNull LoaderManager loaderManager,
                    @NonNull MovieRepository movieRepository,
                    @NonNull DetailContract.View view) {
        mMovie = movie;
        mMovieRepository = movieRepository;
        //mLoaderManager = loaderManager;
        mView = view;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        Disposable vs = mMovieRepository.getVideos(mMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        result -> mView.showTrailers(result.getVideos()),
                        e -> mView.showError(e)
                );
        mDisposables.add(vs);

        Disposable rs = mMovieRepository.getReviews(mMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        result -> mView.showReviews(result.getReviews()),
                        e -> mView.showError(e)
                );
        mDisposables.add(rs);

        Disposable rf = mMovieRepository.isFavorite(mMovie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(result -> mIsFavorite = result)
                .subscribe(
                        result -> mView.onFavoriteChanged(result),
                        e -> mView.showError(e)
                );

        mDisposables.add(rf);
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void changeFavorite() {
        Disposable disposable = mMovieRepository.setFavorite(mMovie, !mIsFavorite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(result -> mIsFavorite = result)
                .subscribe(
                        result -> mView.onFavoriteChanged(result),
                        e -> mView.showError(e)
                );
        mDisposables.add(disposable);
    }
}
