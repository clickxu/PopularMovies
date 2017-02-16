package com.clickxu.popularmovies;

import com.clickxu.popularmovies.datasource.Movie;
import com.clickxu.popularmovies.domain.ContentType;

import java.util.List;

/**
 * Created by t-xu on 2/14/17.
 */

public interface MainContract {

    interface View {
        void showContents(List<Movie> movies);
        void clearContents();
        void showLoadError(Throwable t);
    }

    interface Presenter {
        void loadNext();
        boolean isLoading();
        void refresh();
        void onContentTypeSelected(@ContentType int selectedType);


        @ContentType int getContentType();
        int getPage();
        int getTotalPages();
    }
}
