package com.clickxu.popularmovies.top;

import android.database.Cursor;

import com.clickxu.popularmovies.BasePresenter;
import com.clickxu.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by t-xu on 2/14/17.
 */

public interface TopContract {

    interface View {
        void showContents(List<Movie> movies);
        void showContents(Cursor movies);
        void clearContents();
        void showLoadError(Throwable t);
    }

    interface Presenter extends BasePresenter {
        void loadNext();
        boolean isLoading();
        void refresh();
        void onContentTypeSelected(@ContentType int selectedType);


        @ContentType int getContentType();
        int getPage();
        int getTotalPages();
    }
}
