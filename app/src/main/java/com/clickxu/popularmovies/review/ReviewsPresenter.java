package com.clickxu.popularmovies.review;

import android.support.annotation.NonNull;

import com.clickxu.popularmovies.data.Movie;
import com.clickxu.popularmovies.data.MovieRepository;
import com.clickxu.popularmovies.data.ReviewsResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by t-xu on 3/31/17.
 */

public class ReviewsPresenter implements ReviewsContract.Presenter {

    @NonNull
    private final ReviewsContract.View mView;
    @NonNull
    private final MovieRepository mMovieRepository;
    @NonNull
    private final Movie mMovie;
    private int mTotalPages;
    private int mPage;
    private final CompositeDisposable mDisposables;
    private boolean mLoading;

    ReviewsPresenter(@NonNull ReviewsContract.View view,
                     @NonNull MovieRepository movieRepository,
                     @NonNull Movie movie,
                     int totalPages, int page) {
        mView = checkNotNull(view, "ReviewsContract.View cannot be null");
        mMovieRepository = checkNotNull(movieRepository, "MovieRepository cannot be null");
        mMovie = checkNotNull(movie, "Target movie cannot be null");
        mTotalPages = totalPages;
        mPage = page;
        mDisposables = new CompositeDisposable();
        mLoading = false;
    }

    @Override
    public void subscribe() {
        loadNext();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void loadNext() {
        if (!mLoading) {
            int nextPage = mPage + 1;
            if (nextPage <= mTotalPages) {
                mDisposables.clear();
                Disposable s = mMovieRepository.getReviews(mMovie.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::onSuccess, this::onFailure);
                mDisposables.add(s);
            }
        }
    }

    @Override
    public void refresh() {
        mView.clearReviews();
        mPage = 0;
        mTotalPages = Integer.MAX_VALUE;
        loadNext();
    }

    @Override
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public int getTotalPages() {
        return mTotalPages;
    }

    private void onSuccess(ReviewsResult result) {
        dismissLoading();
        mTotalPages = result.getTotalPages();
        int page = result.getPage();
        if (mPage + 1 == page) {
            mPage = page;
            mView.showReviews(result.getReviews());
        }
    }

    private void onFailure(Throwable e) {
        dismissLoading();
        mView.showError(e);
    }

    private void dismissLoading() {
        mLoading = false;
        mView.dismissLoading();
    }
}
