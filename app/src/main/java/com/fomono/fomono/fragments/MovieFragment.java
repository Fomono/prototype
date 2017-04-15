package com.fomono.fomono.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.R;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.movies.MovieResponse;
import com.fomono.fomono.network.client.MovieDBClientRetrofit;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saranu on 4/6/17.
 */

public class MovieFragment extends MainListFragment {


    private final static String TAG = "Movie Fragment";
    MovieDBClientRetrofit movieDBClientRetrofit;
    private boolean initialMoviesLoaded = false;
    public int moviePage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if (internetAlertDialogue.checkForInternet() && !initialMoviesLoaded) {
            populateMovies(moviePage++);
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(internetAlertDialogue.checkForInternet()) {
                    populateMovies(moviePage++);
                }
            }
        });
        return view;
    }

    public void populateMovies(int page) {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        getMoviesNowPlaying(getActivity(), null, page);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

    public void getMoviesNowPlaying(Context context, String stringQuery, int page) {
        movieDBClientRetrofit = MovieDBClientRetrofit.getNewInstance();
        initialMoviesLoaded = true;


        Map<String, String> data = new HashMap<>();
        data.put("api_key", getResources().getString(R.string.movieDB_api_key));
        data.put("page", String.valueOf(page));

        Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                .getNowPlayingMoviesFromServer(data);

        callMovie.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                try {
                    ArrayList<Movie> movies = response.body().getResults();
                    if (movies == null || movies.isEmpty()) {
                        Log.d(TAG, "No movies fetched!!");
                    } else {
                        Movie.saveOrUpdateFromList(movies);
                        fomonoEvents.addAll(movies);
                        fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                        Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }
}
