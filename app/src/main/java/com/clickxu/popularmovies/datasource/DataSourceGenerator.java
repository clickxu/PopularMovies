package com.clickxu.popularmovies.datasource;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.clickxu.popularmovies.ApiConsts.MOVIE_URL;

/**
 * Created by t-xu on 2/2/17.
 */

public class DataSourceGenerator {

    private static final OkHttpClient sOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(70, TimeUnit.SECONDS)
            .addInterceptor(
                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build();
    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .client(sOkHttpClient)
            .baseUrl(MOVIE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private DataSourceGenerator() {
    }

    public static MovieDataSource generate() {
        return sRetrofit.create(MovieDataSource.class);
    }
}
