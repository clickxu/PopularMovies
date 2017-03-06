package com.clickxu.popularmovies.top;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.clickxu.popularmovies.Injection;
import com.clickxu.popularmovies.R;
import com.clickxu.popularmovies.data.Movie;
import com.clickxu.popularmovies.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.clickxu.popularmovies.BuildConfig.IMAGE_URL;
import static com.clickxu.popularmovies.top.ContentType.POP_MOViES;
import static com.clickxu.popularmovies.top.ContentType.TOP_RATED_MOViES;
import static com.clickxu.popularmovies.utils.DisplayUtils.calculateNoOfColumns;
import static com.clickxu.popularmovies.utils.TypeUtils.getContentType;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String CONTENT_TYPE = "MainActivity.ContentType";
    public static final String PAGE = "MainActivity.Page";
    public static final String TOTAL_PAGE = "MainActivity.TotalPage";
    public static final String MOVIES = "MainActivity.Movies";

    MainContract.Presenter mPresenter;
    GridLayoutManager mLayoutManager;
    MoviesAdapter mContentsAdapter;

    @BindView(R.id.movie_contents) RecyclerView mContentsView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<Movie> movies = null;
        int contentType = POP_MOViES;
        int page = 0;
        int totalPage = Integer.MAX_VALUE;

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(MOVIES);
            contentType = getContentType(savedInstanceState.getInt(CONTENT_TYPE, POP_MOViES));
            page = savedInstanceState.getInt(PAGE, 1);
            totalPage = savedInstanceState.getInt(TOTAL_PAGE, Integer.MAX_VALUE);
        }
        mPresenter = new MainPresenter(this, Injection.provideMovieRepository(),
                contentType, page, totalPage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.title_pop);
        }
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pop:
                    mPresenter.onContentTypeSelected(POP_MOViES);
                    return true;
                case R.id.top_rated:
                    mPresenter.onContentTypeSelected(TOP_RATED_MOViES);
                    return true;
            }
            return false;
        });

        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this, 180));
        mContentsView.setLayoutManager(mLayoutManager);
        mContentsAdapter = new MoviesAdapter(movies);
        mContentsView.setAdapter(mContentsAdapter);
        mContentsView.addOnScrollListener(new OnScrollListener() {

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
    protected void onStart() {
        super.onStart();
        if (mContentsAdapter.getItemCount() == 0) {
            mPresenter.subscribe();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES, mContentsAdapter.getMovies());
        outState.putInt(CONTENT_TYPE, mPresenter.getContentType());
        outState.putInt(PAGE, mPresenter.getPage());
        outState.putInt(TOTAL_PAGE, mPresenter.getTotalPages());
    }

    @Override
    public void showContents(List<Movie> movies) {
        mContentsAdapter.append(movies);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clearContents() {
        mContentsAdapter.clearDataSet();
    }

    @Override
    public void showLoadError(Throwable t) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

        private ArrayList<Movie> mMovies;
        private Context mContext;

        MoviesAdapter(ArrayList<Movie> movies) {
            if (movies == null) {
                mMovies = new ArrayList<>();
                return;
            }
            mMovies = movies;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View v = LayoutInflater.from(mContext).inflate(R.layout.grid_movie, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.posterImage.setImageBitmap(null);
            final Movie movie = mMovies.get(position);
            String posterPath = movie.getPosterPath();
            Picasso.with(mContext)
                    .load(IMAGE_URL + posterPath)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(holder.posterImage);
            holder.posterImage.setOnClickListener(v -> DetailActivity.launch(mContext, movie));
        }

        ArrayList<Movie> getMovies() {
            return mMovies;
        }

        void append(List<Movie> movies) {
            if (movies != null) {
                mMovies.addAll(movies);
                notifyDataSetChanged();
            }
        }

        void clearDataSet() {
            mMovies.clear();
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView posterImage;

            ViewHolder(View itemView) {
                super(itemView);
                posterImage = (ImageView) itemView.findViewById(R.id.movie_img);
            }
        }
    }
}
