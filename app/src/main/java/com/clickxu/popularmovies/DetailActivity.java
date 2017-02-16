package com.clickxu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.clickxu.popularmovies.datasource.Movie;
import com.squareup.picasso.Picasso;

import static com.clickxu.popularmovies.ApiConsts.IMAGE_URL;

/**
 * Created by t-xu on 2/2/17.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL = "DetailActivity.MovieDetail";

    Toolbar mToolbar;

    TextView mTitle;
    ImageView mThumbnail;
    TextView mReleaseYear;
    TextView mVideoLength;
    TextView mRating;
    TextView mDescription;

    Movie mMovie;

    public static void launch(Context from, Movie movie) {
        if (movie == null) {
            return;
        }
        Intent i = new Intent();
        i.setClass(from, DetailActivity.class);
        i.putExtra(MOVIE_DETAIL, movie);
        from.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.title_detail);
        }
        mTitle = (TextView) findViewById(R.id.title);
        mReleaseYear = (TextView) findViewById(R.id.releaseYear);
        mVideoLength = (TextView) findViewById(R.id.videoLength);
        mRating = (TextView) findViewById(R.id.rating);
        mDescription = (TextView) findViewById(R.id.description);
        mThumbnail = (ImageView) findViewById(R.id.thumbnail);

        mMovie = getIntent().getParcelableExtra(MOVIE_DETAIL);
        mTitle.setText(mMovie.getTitle());
        mReleaseYear.setText(mMovie.getReleaseDate());
        mRating.setText("" + mMovie.getVoteAverage());
        mDescription.setText(mMovie.getOverview());
        Picasso.with(this)
                .load(IMAGE_URL + mMovie.getPosterPath())
                .noPlaceholder()
                .into(mThumbnail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
