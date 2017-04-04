package com.clickxu.popularmovies.detail;

import com.clickxu.popularmovies.BasePresenter;
import com.clickxu.popularmovies.data.Review;
import com.clickxu.popularmovies.data.Video;

import java.util.List;

/**
 * Created by t-xu on 2/26/17.
 */

public interface DetailContract {

    interface View {
        void showTrailers(List<Video> trailers);
        void showError(Throwable e);
        void onFavoriteChanged(boolean isFavorite);
    }

    interface Presenter extends BasePresenter {
        void changeFavorite();
    }
}
