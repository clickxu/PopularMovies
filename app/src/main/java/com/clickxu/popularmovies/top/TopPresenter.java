package com.clickxu.popularmovies.top;

import com.clickxu.popularmovies.data.MoviesResult;
import com.clickxu.popularmovies.data.MovieRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;

/**
 * Created by t-xu on 2/14/17.
 */
class TopPresenter implements TopContract.Presenter {

    private TopContract.View mView;
    private MovieRepository mMovieRepository;
    @ContentType
    private int mContentType;
    private int mPage;
    private int mTotalPages;

    private CompositeDisposable mDisposables;
    private boolean mLoading;

    TopPresenter(TopContract.View view, MovieRepository movieRepository,
                 @ContentType int contentType, int page, int totalPages) {
        mView = view;
        mMovieRepository = movieRepository;
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
}
