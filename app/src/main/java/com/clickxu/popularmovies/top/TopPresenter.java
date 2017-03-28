package com.clickxu.popularmovies.top;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.clickxu.popularmovies.data.LoaderProvider;
import com.clickxu.popularmovies.data.MoviesResult;
import com.clickxu.popularmovies.data.MovieRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.clickxu.popularmovies.top.ContentType.FAVORITE_MOViES;
import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by t-xu on 2/14/17.
 */
class TopPresenter implements TopContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;

    private TopContract.View mView;
    @NonNull
    private MovieRepository mMovieRepository;
    @NonNull
    private final LoaderManager mLoaderManager;
    @NonNull
    private final LoaderProvider mLoaderProvider;

    @ContentType
    private int mContentType;
    private int mPage;
    private int mTotalPages;

    private CompositeDisposable mDisposables;
    private boolean mLoading;

    TopPresenter(TopContract.View view, MovieRepository movieRepository,
                 LoaderProvider loaderProvider, LoaderManager loaderManager,
                 @ContentType int contentType, int page, int totalPages) {
        mView = checkNotNull(view, "TopContract.View cannot be null");
        mMovieRepository = checkNotNull(movieRepository, "MovieRepository cannot be null");
        mLoaderProvider = checkNotNull(loaderProvider, "LoaderProvider cannot be null");
        mLoaderManager = checkNotNull(loaderManager, "LoaderManager cannot be null");
        mContentType = contentType;
        mPage = page;
        mTotalPages = totalPages;
        mDisposables = new CompositeDisposable();
        mLoading = false;
    }

    @Override
    public void loadNext() {
        if (!mLoading) {
            int nextPage = mPage + 1;
            if (nextPage <= mTotalPages) {
                mDisposables.clear();
                Disposable s = null;
                switch (mContentType) {
                    case POP_MOViES:
                        s = mMovieRepository.getPopularMovies(nextPage)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(this::onSuccess, this::onFailure);
                        break;
                    case TOP_RATED_MOViES:
                        s = mMovieRepository.getTopRatedMovies(nextPage)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(this::onSuccess, this::onFailure);
                        break;
                    case FAVORITE_MOViES:
                        mLoaderManager.initLoader(CURSOR_LOADER_ID, null, this);
                        break;
                }
                if (s != null) mDisposables.add(s);
            }
        }
        mLoading = true;
    }

    @Override
    public void refresh() {
        mView.clearContents();
        mPage = 0;
        mTotalPages = Integer.MAX_VALUE;
        loadNext();
    }

    @Override
    public void onContentTypeSelected(@ContentType int selectedType) {
        mDisposables.clear();
        mLoading = false;
        if (selectedType != mContentType) {
            mContentType = selectedType;
            refresh();
        }
    }

    @Override
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public int getContentType() {
        return mContentType;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public int getTotalPages() {
        return mTotalPages;
    }

    private void onSuccess(MoviesResult result) {
        mLoading = false;
        mTotalPages = result.getTotalPages();
        int page = result.getPage();
        if (mPage + 1 == page) {
            mPage = page;
            mView.showContents(result.getMovies());
        }
    }

    private void onFailure(Throwable e) {
        mLoading = false;
        mView.showLoadError(e);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoading = false;
        return mLoaderProvider.createFavoriteMoviesLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoading = false;
        mView.showContents(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLoading = false;
        mView.clearContents();
    }
}
