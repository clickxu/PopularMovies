package com.clickxu.popularmovies.top;

import com.clickxu.popularmovies.data.MoviesResult;
import com.clickxu.popularmovies.data.MovieRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;

/**
 * Created by t-xu on 2/14/17.
 */
class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private MovieRepository mMovieRepository;
    @ContentType
    private int mContentType;
    private int mPage;
    private int mTotalPages;

    private CompositeSubscription mSubscriptions;
    private boolean mLoading;

    MainPresenter(MainContract.View view, MovieRepository movieRepository,
                  @ContentType int contentType, int page, int totalPages) {
        mView = view;
        mMovieRepository = movieRepository;
        mContentType = contentType;
        mPage = page;
        mTotalPages = totalPages;
        mSubscriptions = new CompositeSubscription();
        mLoading = false;
    }

    @Override
    public void loadNext() {
        if (!mLoading) {
            int nextPage = mPage + 1;
            if (nextPage <= mTotalPages) {
                mSubscriptions.clear();
                Subscription s = null;
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
                if (s != null) mSubscriptions.add(s);
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
        mSubscriptions.clear();
    }
}
