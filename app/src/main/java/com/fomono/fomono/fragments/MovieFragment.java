package com.fomono.fomono.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.R;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.movies.MovieResponse;
import com.fomono.fomono.network.client.MovieDBClientRetrofit;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fomono.fomono.network.client.MovieDBClientRetrofit.API_KEY;

/**
 * Created by Saranu on 4/6/17.
 */

public class MovieFragment extends MainListFragment {


    private final static String TAG = "Movie Fragment";
    MovieDBClientRetrofit movieDBClientRetrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if (internetAlertDialogue.checkForInternet()) {
            populateMovies();
        }
        return view;
    }

    public void populateMovies() {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
     //   getRecentMovies();
        getMoviesNowPlaying(getActivity(), null);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

    public void getMoviesNowPlaying(Context context, String stringQuery) {
        movieDBClientRetrofit = MovieDBClientRetrofit.getNewInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = movieDBClientRetrofit.MovieDBRetrofitClientFactory().getNowPlayingMoviesFromServer(getResources().getString(R.string.movieDB_api_key));
        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> movies = response.body().getResults();
                if (movies == null || movies.isEmpty()) {
                    Log.d(TAG, "No movies fetched!!");
                } else {
                    fomonoEvents.addAll(movies);
                    fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }
}
