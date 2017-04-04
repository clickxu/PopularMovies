package com.clickxu.popularmovies.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clickxu.popularmovies.Injection;
import com.clickxu.popularmovies.R;
import com.clickxu.popularmovies.data.Movie;
import com.clickxu.popularmovies.data.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by t-xu on 3/31/17.
 */

public class ReviewsActivity extends AppCompatActivity implements ReviewsContract.View   {

    private static final String TARGET_MOVIE = "ReviewsActivity.TargetMovie";
    public static final String PAGE = "ReviewsActivity.Page";
    public static final String TOTAL_PAGE = "ReviewsActivity.TotalPage";
    public static final String REVIEWS = "MainActivity.Reviews";

    @BindView(R.id.reviews) RecyclerView mContentsView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    Movie mMovie;
    ReviewsContract.Presenter mPresenter;
    ReviewsAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    public static void launch(@NonNull Context from, @NonNull Movie movie) {
        Intent i = new Intent();
        i.setClass(from, ReviewsActivity.class);
        i.putExtra(TARGET_MOVIE, movie);
        from.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        ArrayList<Review> reviews = null;
        int page = 0;
        int totalPage = Integer.MAX_VALUE;
        if (savedInstanceState != null) {
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS);
            page = savedInstanceState.getInt(PAGE, 1);
            totalPage = savedInstanceState.getInt(TOTAL_PAGE, Integer.MAX_VALUE);
        }
        mMovie = getIntent().getParcelableExtra(TARGET_MOVIE);
        mPresenter = new ReviewsPresenter(this,
                Injection.provideMovieRepository(getContentResolver()),
                mMovie, totalPage, page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mMovie.getTitle());
        }

        mLayoutManager = new LinearLayoutManager(this);
        mContentsView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewsAdapter(reviews);
        mContentsView.setAdapter(mAdapter);
        mContentsView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            final int visibleThreshold = 5;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (!mPresenter.isLoading() && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        mPresenter.loadNext();
                    }
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.refresh());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE, mPresenter.getPage());
        outState.putInt(TOTAL_PAGE, mPresenter.getTotalPages());
        outState.putParcelableArrayList(REVIEWS, mAdapter.getReviews());
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
    public void showReviews(List<Review> reviews) {
        mAdapter.append(reviews);
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(this, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clearReviews() {
        mAdapter.clearDataSet();
    }

    static class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>  {

        ArrayList<Review> mReviews;

        ReviewsAdapter(ArrayList<Review> reviews) {
            if (reviews == null) {
                mReviews = new ArrayList<>();
                return;
            }
            mReviews = reviews;
        }

        ArrayList<Review> getReviews() {
            return mReviews;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_review, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Review review = mReviews.get(position);
            holder.authorText.setText(review.getAuthor());
            holder.contentText.setText(review.getContent());
            Linkify.addLinks(holder.contentText, Linkify.WEB_URLS);
        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }

        void append(List<Review> reviews) {
            if (reviews != null) {
                mReviews.addAll(reviews);
                notifyDataSetChanged();
            }
        }

        void clearDataSet() {
            mReviews.clear();
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView authorText;
            TextView contentText;

            ViewHolder(View itemView) {
                super(itemView);
                authorText = (TextView) itemView.findViewById(R.id.author);
                contentText = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }
}
