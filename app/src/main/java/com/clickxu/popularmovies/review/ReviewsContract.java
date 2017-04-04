package com.clickxu.popularmovies.review;

import com.clickxu.popularmovies.BasePresenter;
import com.clickxu.popularmovies.data.Review;

import java.util.List;

/**
 * Created by t-xu on 3/31/17.
 */

public interface ReviewsContract {

    interface View {
        void showReviews(List<Review> reviews);
        void showError(Throwable e);
        void dismissLoading();
        void clearReviews();
    }

    interface Presenter extends BasePresenter {
        void loadNext();
        void refresh();
        boolean isLoading();
        int getPage();
        int getTotalPages();
    }
}
