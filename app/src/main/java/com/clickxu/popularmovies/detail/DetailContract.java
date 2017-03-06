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
        void showReviews(List<Review> reviews);
    }

    interface Presenter extends BasePresenter {

    }
}
