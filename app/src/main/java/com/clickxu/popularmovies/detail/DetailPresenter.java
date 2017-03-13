package com.clickxu.popularmovies.detail;

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
    private DetailContract.View mView;
    private MovieRepository mMovieRepository;
    private CompositeDisposable mDisposables;

    DetailPresenter(DetailContract.View view, MovieRepository movieRepository, Movie movie) {
        mView = view;
        mMovieRepository = movieRepository;
        mMovie = movie;
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
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
