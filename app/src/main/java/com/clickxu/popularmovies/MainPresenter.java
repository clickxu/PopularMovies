package com.clickxu.popularmovies;

import com.clickxu.popularmovies.datasource.DataSourceGenerator;
import com.clickxu.popularmovies.datasource.MovieDataSource;
import com.clickxu.popularmovies.datasource.MoviesResult;
import com.clickxu.popularmovies.domain.ContentType;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.clickxu.popularmovies.domain.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.domain.ContentType.TOP_RATED_MOViES;

/**
 * Created by t-xu on 2/14/17.
 */
class MainPresenter implements MainContract.Presenter, Callback<MoviesResult> {

    private String mApiKey;
    private MainContract.View mView;

    private MovieDataSource mMovieDataSource;
    @ContentType
    private int mContentType;
    private int mPage;
    private int mTotalPages;

    private boolean mLoading;

    MainPresenter(MainContract.View view, String apiKey,
                  @ContentType int contentType, int page, int totalPages) {
        mApiKey = apiKey;
        mView = view;
        mContentType = contentType;
        mPage = page;
        mTotalPages = totalPages;
        mMovieDataSource = DataSourceGenerator.generate();
        mLoading = false;
    }

    @Override
    public void loadNext() {
        if (!mLoading) {
            int nextPage = mPage + 1;
            if (nextPage <= mTotalPages) {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", mApiKey);
                params.put("page", "" + nextPage);
                switch (mContentType) {
                    case POP_MOViES:
                        mMovieDataSource.getPopularMovies(params).enqueue(this);
                        break;
                    case TOP_RATED_MOViES:
                        mMovieDataSource.getTopRatedMovies(params).enqueue(this);
                        break;
                }
            }
            mLoading = true;
        }
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

    @Override
    public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
        mLoading = false;
        mTotalPages = response.body().getTotalPages();
        int page = response.body().getPage();
        if (mPage + 1 == page) {
            mPage = page;
            mView.showContents(response.body().getResults());
        }
    }

    @Override
    public void onFailure(Call<MoviesResult> call, Throwable t) {
        mLoading = false;
        mView.showLoadError(t);
    }
}
