package com.clickxu.popularmovies.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clickxu.popularmovies.Injection;
import com.clickxu.popularmovies.R;
import com.clickxu.popularmovies.data.Movie;
import com.clickxu.popularmovies.data.Review;
import com.clickxu.popularmovies.data.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clickxu.popularmovies.BuildConfig.IMAGE_URL;

/**
 * Created by t-xu on 2/2/17.
 */

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String MOVIE_DETAIL = "DetailActivity.MovieDetail";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.thumbnail) ImageView mThumbnail;
    @BindView(R.id.releaseYear) TextView mReleaseYear;
    @BindView(R.id.videoLength) TextView mVideoLength;
    @BindView(R.id.rating) TextView mRating;
    @BindView(R.id.description) TextView mDescription;
    @BindView(R.id.trailers) LinearLayout mTrailers;
    @BindView(R.id.favorite) Button mFavorite;

    Movie mMovie;
    DetailContract.Presenter mPresenter;

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
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.title_detail);
        }
        mMovie = getIntent().getParcelableExtra(MOVIE_DETAIL);
        mTitle.setText(mMovie.getTitle());
        mReleaseYear.setText(mMovie.getReleaseDate());
        mRating.setText("" + mMovie.getVoteAverage());
        mDescription.setText(mMovie.getOverview());
        Picasso.with(this)
                .load(IMAGE_URL + mMovie.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(mThumbnail);
       mPresenter = new DetailPresenter(mMovie,
               Injection.provideMovieRepository(getContentResolver()), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public void showTrailers(List<Video> trailers) {
        mTrailers.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Video video : trailers) {
            View row = inflater.inflate(R.layout.row_trailer, mTrailers, false);
            TextView tv = (TextView) row.findViewById(R.id.trailer_title);
            tv.setText(video.getName());
            row.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + video.getKey()));
                startActivity(intent);
            });
            mTrailers.addView(row);
            View.inflate(this, R.layout.trailer_divider, mTrailers);
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {
        //TODO
    }

    @Override
    public void showError(Throwable e) {
        Log.e("showError", e.getMessage(), e);
        Toast.makeText(this, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteChanged(boolean isFavorite) {
        if (isFavorite) {
            mFavorite.setText(R.string.remove_from_favorite);
        } else {
            mFavorite.setText(R.string.mark_as_favorite);
        }
        mFavorite.setEnabled(true);
    }

    @OnClick(R.id.favorite)
    public void onClickFavorite(Button button) {
        button.setEnabled(false);
        mPresenter.changeFavorite();
    }
}
